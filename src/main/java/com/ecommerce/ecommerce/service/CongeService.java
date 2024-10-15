package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.Conge;
import com.ecommerce.ecommerce.entity.DtoUtilisateur;
import com.ecommerce.ecommerce.entity.Role;
import com.ecommerce.ecommerce.entity.Utilisateur;
import com.ecommerce.ecommerce.repository.CongeRepository;
import com.ecommerce.ecommerce.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Conge> getCongesByStatus(String status) {
        return congeRepository.findByStatus(status);
    }
    public List<Conge> findByRole(Role role) {
        return congeRepository.findByRole(role);
    }
    public List<Conge> findAllByUser(String idU) {
        return congeRepository.findByUtilisateurIdU(idU); // Assurez-vous que cette méthode existe dans votre repository
    }
    public List<Conge> findbyUsername(String username) {
        return congeRepository.findByUsername(username);
    }
    public List<Conge> searchConges(String status, Role role, Date dateDebut, Date endDate) {

        if (status != null && !status.isEmpty()) {
            return congeRepository.findByStatus(status);
        } else if (role != null ) {
            return congeRepository.findByRole(role);
        } else if (dateDebut != null && endDate != null) {
            return congeRepository.findByDateRange(dateDebut, endDate);
        }

        // Fallback case: return all if no parameters
        return congeRepository.findAll();
    }
    public String updateSoldeConges(String congeId) {
        Optional<Conge> optionalConge = congeRepository.findById(congeId);

        if (!optionalConge.isPresent()) {
            return "Congé non trouvé";
        }

        Conge conge = optionalConge.get();
        long daysBetween = ChronoUnit.DAYS.between(conge.getDateDebut().toInstant(), conge.getDateFin().toInstant()) + 1; // Inclure la date de fin

        Utilisateur utilisateur = conge.getUtilisateur();
        if (utilisateur == null) {
            return "Utilisateur non trouvé pour ce congé";
        }

        int nouveauSolde = utilisateur.getSoldeConges() - (int) daysBetween;
        utilisateur.setSoldeConges(nouveauSolde);

        utilisateurRepository.save(utilisateur);

        return "Solde de congés mis à jour avec succès";
    }
    private int calculateVacationDays(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return (int) (diffInMillies / (1000 * 60 * 60 * 24));
    }
    public Conge findById(String idC) {
        Optional<Conge> conge = congeRepository.findById(idC);
        return conge.orElseThrow(() -> new ResourceNotFoundException("Congé non trouvé avec l'ID " + idC));
    }
    public Conge updateSolde(String idC, Integer newSolde) {
        Conge conge = findById(idC); // Cela lancera une exception si non trouvé
        conge.setCountVacation(newSolde); // Met à jour le solde
        return congeRepository.save(conge);
    }

}
