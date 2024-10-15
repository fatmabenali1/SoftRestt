package com.ecommerce.ecommerce.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "utilisateurs")
public class Utilisateur implements UserDetails {
    @Id
    private String idU;
    private String username;
    private String nom;
    private String email;
    private String password;  // Ajout du mot de passe
    private Role role;
    private int soldeConges = 30;
    private int countVacation = 30;
    @DBRef
    private List<Conge> conges;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    return null ;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }
}
