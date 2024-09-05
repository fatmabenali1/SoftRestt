package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.Token;
import com.ecommerce.ecommerce.entity.Utilisateur;
import com.ecommerce.ecommerce.repository.TokenRepository;
import com.ecommerce.ecommerce.repository.UtilisateurRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
private final UtilisateurRepository utilisateurRepository;
private final UserService userService;
private final TokenRepository tokenRepository;
    public String genererToken(String username){
      Utilisateur utilisateur = userService.loadUserByUsername(username);
        final Token token = Token.builder().utilisateur(utilisateur).expire(false).desactive(false).jwt(generateJwt(utilisateur)).build();
    //    tokenRepository.save(token);
        return generateJwt(utilisateur);
    }

    private String generateJwt(UserDetails utilisateur) {
       return Jwts.builder().setIssuedAt(new Date(System.currentTimeMillis())).
               setExpiration(new Date(System.currentTimeMillis()+1000*60*30)).
                setSubject(utilisateur.getUsername()).
                signWith(generateKey(), SignatureAlgorithm.HS256).compact();
       //return Map.of("bearer",bearer);
    }

    private Key generateKey() {
        byte[] key = Decoders.BASE64.decode("8cbc4b6546aafa856a2e8b7f88068b66acdf107e2e4fb20cf64cb209e813d4dd");
        return Keys.hmacShaKeyFor(key);
    }
    public <T> T extactClaim(String token, Function<Claims,T>claimsResolver){
        final Claims claims = extracAllClaims(token);
         return claimsResolver.apply(claims);
    }
    private Claims extracAllClaims(String token){
        return Jwts.parserBuilder().
                setSigningKey(generateKey()).
                build().parseClaimsJws(token).
                getBody();
    }
    public String getUsername(String token){
        return extactClaim(token,Claims::getSubject);
    }
    public Date expiration(String token){
        return extactClaim(token,Claims::getExpiration);
    }
    public boolean isTokenExpired(String token){
       return expiration(token).before(new Date());
    }

    public boolean validationToken(String token, UserDetails userDetails){
        final String username = getUsername(token);
        return ((userDetails.getUsername()).equals(username)&& !isTokenExpired(token));
    }
}
