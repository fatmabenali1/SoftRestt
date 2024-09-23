package com.ecommerce.ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import com.ecommerce.ecommerce.service.BlacklistTokenService;
import com.ecommerce.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final BlacklistTokenService blacklistTokenService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/inscription", "/connexion").permitAll() // Allow these without auth
                        .requestMatchers(HttpMethod.GET,"/conges/").authenticated()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().permitAll() // Require authentication for all other requests
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtConfig, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtConfig, AnonymousAuthenticationFilter.class) // Add JwtConfig filter before AnonymousAuthenticationFilter
                .logout(logout -> logout
                        .logoutUrl("/logout") // Set the logout URL
                        .invalidateHttpSession(true) // Invalidate the session
                        .clearAuthentication(true) // Clear authentication
                        .deleteCookies("JSESSIONID") // Optional: delete session cookies
                        .addLogoutHandler(new LogoutHandler() {
                            @Override
                            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                                String authorizationHeader = request.getHeader("Authorization");
                                if (authorizationHeader != null && authorizationHeader.length() > 7) {
                                    String token = authorizationHeader.substring(7);
                                    blacklistTokenService.blacklistToken(token); // Blacklist the token after logout
                                }
                            }
                        })
                        .logoutSuccessHandler(new LogoutSuccessHandler() {
                            @Override
                            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("Déconnexion réussie");
                            }
                        })
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }
}