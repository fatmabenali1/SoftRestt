package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.Conge;
import com.ecommerce.ecommerce.repository.CongeRepository;
import com.ecommerce.ecommerce.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CongeService {

    @Autowired
    private CongeRepository congeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Conge> getAllConges() {
        return congeRepository.findAll();
    }

    public Conge demanderConge(Conge conge) {
        conge.setStatus("En attente");
        return congeRepository.save(conge);
    }

    public Conge validerCongeParTechlead(String congeId) {
        Conge conge = congeRepository.findById(congeId).orElseThrow();
        conge.setStatus("Validé Techlead");
        return congeRepository.save(conge);
    }

    public Conge validerCongeParRH(String congeId) {
        Conge conge = congeRepository.findById(congeId).orElseThrow();
        conge.setStatus("Validé RH");
        conge.setDateValidation(new Date());
        return congeRepository.save(conge);
    }
}