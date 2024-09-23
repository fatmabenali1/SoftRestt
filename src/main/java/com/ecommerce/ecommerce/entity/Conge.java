package com.ecommerce.ecommerce.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Document(collection = "conges")
public class Conge {
    @Id
    private String idC;
    private Date dateDebut;
    private Date dateFin;
    private String reason;
    private String status = "Pending";
    private int soldeConges;
    private Date dateValidation;
    private String title;
    @DBRef
    private Utilisateur utilisateur;

    // Add a getter for the utilisateur's role, with a null check

}