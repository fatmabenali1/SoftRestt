package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.entity.*;
import com.ecommerce.ecommerce.service.CongeService;
import com.ecommerce.ecommerce.service.JwtService;
import com.ecommerce.ecommerce.service.UserService;
import com.ecommerce.ecommerce.service.BlacklistTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UtilisateurController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CongeService congeService;
    private final BlacklistTokenService blacklistTokenService;
    @CrossOrigin(origins = "*")

    // Remove this method
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            // Extraire le token du header Authorization
            String token = jwtService.extractTokenFromRequest(request);

            if (token == null) {
                return ResponseEntity.badRequest().body("Token non fourni ou invalide");
            }

            // Vérifier si le token est déjà blacklisté
            if (blacklistTokenService.isBlacklisted(token)) {
                return ResponseEntity.badRequest().body("Token déjà blacklisté");
            }

            // Ajouter le token à la blacklist
            blacklistTokenService.addToBlacklist(token);

            // Supprimer le contexte de sécurité (déconnexion de l'utilisateur)
            new SecurityContextLogoutHandler().logout(request, response, auth);
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok("Déconnexion réussie et token blacklisté");
        }

        return ResponseEntity.badRequest().body("Utilisateur non authentifié");
    }

    @PostMapping("inscription")
    public ResponseEntity<Utilisateur> inscription(@RequestBody Utilisateur utilisateur) {
        Utilisateur createdUser = userService.inscription(utilisateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @PostMapping("connexion")
    public ResponseEntity<TokenAuth> login(@RequestBody DtoUtilisateur utilisateur) {
        try {
            // Authentifier l'utilisateur
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(utilisateur.getUsername(), utilisateur.getPassword()));

            if (auth.isAuthenticated()) {
                // Générer le token JWT
                String token = jwtService.genererToken(utilisateur.getUsername());

                // Récupérer les détails de l'utilisateur authentifié
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                String username = userDetails.getUsername();

                // Récupérer l'utilisateur depuis la base de données
                Utilisateur userFromDb = userService.loadUserByUsername(username);
                if (userFromDb == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                }

                // Récupérer les informations de l'utilisateur
                String email = userFromDb.getEmail();
                String idU = userFromDb.getIdU(); // Assurez-vous que l'ID est bien stocké en tant que chaîne ou Long
                Role role = userFromDb.getRole(); // Récupérer le rôle de l'utilisateur

                // Créer une réponse avec le token, le nom d'utilisateur, l'email, l'ID utilisateur et le rôle
                TokenAuth response = new TokenAuth(token, username, email, role, idU);

                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}

