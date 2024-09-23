package com.ecommerce.ecommerce.entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tokens")  // Specify the collection name, if needed
public class Token {
    @Id
    private String id;  // MongoDB uses String for IDs, typically as ObjectId
    private String jwt;
    private boolean expire;
    private boolean desactive;
    private boolean loggedOut;
    @DBRef
    private Utilisateur utilisateur;  // DBRef creates a reference to another document

    public Token(String token) {
        this.jwt = token;
    }
    @DBRef
    private List<Token> tokens;
}
