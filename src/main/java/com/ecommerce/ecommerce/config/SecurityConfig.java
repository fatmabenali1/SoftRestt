package com.ecommerce.ecommerce.config;
import com.ecommerce.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtConfig jwtConfig;
    private final PasswordEncoderCrypted passwordEncoder;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return   http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers("/**","/error").permitAll()  // Permet l'accès à tous les endpoints sous /api/auth
                                        .requestMatchers("/api/conges/all").authenticated()  // Authentification requise pour ce chemin
                                        .requestMatchers("/api/conges/demander").authenticated()  // Authentification requise pour ce chemin
                                        .requestMatchers("/api/conges/valider-techlead/**").hasRole("TECHLEAD")  // Rôle TECHLEAD requis
                                        .requestMatchers("/api/conges/valider-rh/**").hasRole("RH")
                                        .anyRequest().authenticated()
                ).sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtConfig, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

   /*@Bean
    SecurityWebFilterChain http(ServerHttpSecurity https) throws Exception {
        DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
                new WebSessionServerLogoutHandler(), new SecurityContextServerLogoutHandler()
        );

        https
                .authorizeExchange((exchange) -> exchange.anyExchange().authenticated())
                .logout((logout) -> logout.logoutHandler(logoutHandler));
                 return https.build();
    }*/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder.passwordEncoder());
        return daoAuthenticationProvider;

    }



}
