package com.ecommerce.ecommerce.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Data
@Document(collection = "conges")
public class Conge {
    @Id
    private String idC;
    private Date dateDebut;
    private Date dateFin;
    private String status;  // "En attente", "Validé Techlead", "Validé RH", "Refusé"
    private Date dateValidation;
}
