package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.entity.DtoUtilisateur;
import com.ecommerce.ecommerce.entity.Token;
import com.ecommerce.ecommerce.entity.TokenAuth;
import com.ecommerce.ecommerce.entity.Utilisateur;
import com.ecommerce.ecommerce.service.JwtService;
import com.ecommerce.ecommerce.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UtilisateurController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    //@PreAuthorize("hasAuthority('Role_Client')")
    @GetMapping("test")
    public String test(){
        return "c'est un test !!";
    }





    @PostMapping("inscription")
    public Utilisateur inscription(@RequestBody Utilisateur utilisateur){
      return userService.inscription(utilisateur);
    }




    @PostMapping("connexion")
    public TokenAuth connexion(@RequestBody DtoUtilisateur utilisateur){
       Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(utilisateur.getUsername(),utilisateur.getPassword()));
       if(auth.isAuthenticated()){
          String token= jwtService.genererToken(utilisateur.getUsername());
          return new TokenAuth(token);
       }
        return null;
    }
}
