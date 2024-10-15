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
    private Date dateValidation;
    private String title;
    private String nom;
   private int countVacation = 30;

    @DBRef
    private Utilisateur utilisateur;


}