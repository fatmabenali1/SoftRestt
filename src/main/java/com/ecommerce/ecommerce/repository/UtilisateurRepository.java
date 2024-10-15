package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Role;
import com.ecommerce.ecommerce.entity.Utilisateur;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends MongoRepository<Utilisateur, String> {
    Optional<Utilisateur> findUtilisateurByUsername(String username);
    List<Utilisateur> findByRole(Role role);



}
