package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.entity.Conge;
import com.ecommerce.ecommerce.entity.DtoUtilisateur;
import com.ecommerce.ecommerce.entity.Role;
import com.ecommerce.ecommerce.repository.UtilisateurRepository;
import com.ecommerce.ecommerce.service.CongeService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/conges")
@Slf4j
@CrossOrigin(origins = "*")
public class CongeController {
    private final CongeService congeService;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping("/add/{iduser}")
    public Conge addOrUpdate(@Valid @RequestBody Conge conge, @PathVariable("iduser") String iduser) {
        return congeService.addConge(conge, iduser);
    }
    @GetMapping("/{status}")
    public List<Conge> getCongesByStatus(@PathVariable String status) {
        return congeService.getCongesByStatus(status);
    }
    @GetMapping("/role")
    public List<Conge> getCongesByRole(@PathVariable Role role) {
        return congeService.findByRole(role);
    }
    @GetMapping
    public ResponseEntity<List<Conge>> findAll() {
        return ResponseEntity.ok(congeService.findAll());
    }
    @GetMapping("/utilisateur/{idU}")
    public ResponseEntity<List<Conge>> findAllByUser(@PathVariable String idU) {
        List<Conge> conges = congeService.findAllByUser(idU);
        return ResponseEntity.ok(conges);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Conge>> searchConges(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Date dateDebut,
            @RequestParam(required = false) Date endDate) {

        List<Conge> conges = congeService.searchConges(status, role, dateDebut, endDate);
        return ResponseEntity.ok(conges);
    }
    @GetMapping("/username/{username}")
    public List<Conge> getUsername(@PathVariable String username) {
        return congeService.findbyUsername(username);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Conge> updateConge(@PathVariable String id, @Valid @RequestBody Conge congeDetails) {
        Conge updatedConge = congeService.updateConge(id, congeDetails);
        if (updatedConge != null) {
            return ResponseEntity.ok(updatedConge);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConge(@PathVariable("id") String id) {
        congeService.deleteConge(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("An error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }
    @GetMapping("/update-solde/{congeId}")
    public ResponseEntity<String> updateSolde(@PathVariable String congeId) {
        String result = congeService.updateSoldeConges(congeId);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/{id}/solde")
    public ResponseEntity<Conge> updateSolde(@PathVariable String idC, @RequestBody Map<String, Object> updates) {
        Integer newSolde = (Integer) updates.get("countVacation");
        Conge updatedConge = congeService.updateSolde(idC, newSolde);
        return ResponseEntity.ok(updatedConge);
    }


}