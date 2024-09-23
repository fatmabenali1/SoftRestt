package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.Conge;
import com.ecommerce.ecommerce.entity.Utilisateur;
import com.ecommerce.ecommerce.repository.CongeRepository;
import com.ecommerce.ecommerce.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CongeService {

    private final CongeRepository congeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public Conge addConge(Conge conge, String idUser) {
        Utilisateur user = utilisateurRepository.findById(idUser).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("Utilisateur avec l'ID spécifié n'existe pas.");
        }
        conge.setUtilisateur(user);
        return congeRepository.save(conge);
    }


    public List<Conge> getAllConges() {
        return congeRepository.findAll();
    }

    public Optional<Conge> getCongeById(String id) {
        return congeRepository.findById(id);
    }

    public Conge updateConge(String id, Conge congeDetails) {
        Optional<Conge> congeOptional = congeRepository.findById(id);
        if (congeOptional.isPresent()) {
            Conge conge = congeOptional.get();
            conge.setDateDebut(congeDetails.getDateDebut());
            conge.setDateFin(congeDetails.getDateFin());
            conge.setStatus(congeDetails.getStatus());
            conge.setDateValidation(congeDetails.getDateValidation());

            // Vérifiez si l'utilisateur est présent et valide
            if (congeDetails.getUtilisateur() != null && congeDetails.getUtilisateur().getIdU() != null) {
                Utilisateur utilisateur = utilisateurRepository.findById(congeDetails.getUtilisateur().getIdU()).orElse(null);
                if (utilisateur != null) {
                    conge.setUtilisateur(utilisateur);
                } else {
                    throw new IllegalArgumentException("L'utilisateur associé n'existe pas.");
                }
            }
            return congeRepository.save(conge);
        }
        throw new IllegalArgumentException("Le congé avec cet ID n'existe pas.");
    }

    public void deleteConge(String id) {
        if (!congeRepository.existsById(id)) {
            throw new IllegalArgumentException("Le congé avec cet ID n'existe pas.");
        }
        congeRepository.deleteById(id);
    }

    public List<Conge> findAll() {
        return congeRepository.findAll();
    }

}
