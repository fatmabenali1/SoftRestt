package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.entity.Conge;
import com.ecommerce.ecommerce.service.CongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conges")
public class CongeController {

    @Autowired
    private CongeService congeService;

    @GetMapping("/all")
    public List<Conge> getAllConges() {
        return congeService.getAllConges();
    }

    @PostMapping("/demander")
    public Conge demanderConge(@RequestBody Conge conge) {
        return congeService.demanderConge(conge);
    }

    @PostMapping("/valider-techlead/{id}")
    public Conge validerCongeParTechlead(@PathVariable String id) {
        return congeService.validerCongeParTechlead(id);
    }

    @PostMapping("/valider-rh/{id}")
    public Conge validerCongeParRH(@PathVariable String id) {
        return congeService.validerCongeParRH(id);
    }
}
