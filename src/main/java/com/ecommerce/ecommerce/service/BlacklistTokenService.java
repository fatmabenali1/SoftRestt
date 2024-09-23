package com.ecommerce.ecommerce.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BlacklistTokenService {

    // Utilisation d'un ensemble pour stocker les tokens blacklistés
    private final Set<String> blacklistedTokens = new HashSet<>();


    /**
     * Ajoute un token à la liste noire.
     *
     * @param token Le token à ajouter à la blacklist.
     */
    public void blacklistToken(String token) {
        if (token != null && !token.trim().isEmpty()) {
            blacklistedTokens.add(token);
        }
    }

    /**
     * Vérifie si un token est blacklisté.
     *
     * @param token Le token à vérifier.
     * @return true si le token est blacklisté, sinon false.
     */
    public boolean isTokenBlacklisted(String token) {
        return token != null && blacklistedTokens.contains(token);
    }

    /**
     * Supprime un token de la blacklist (facultatif).
     *
     * @param token Le token à supprimer de la blacklist.
     */
    public void removeTokenFromBlacklist(String token) {
        blacklistedTokens.remove(token);
    }

    // Utilisation d'un Set pour stocker les tokens blacklistés en mémoire (ou utiliser Redis pour une solution persistante)

    // Ajouter un token à la blacklist
    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    // Vérifier si un token est dans la blacklist
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
