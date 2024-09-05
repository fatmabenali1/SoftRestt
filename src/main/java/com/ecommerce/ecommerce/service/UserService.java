package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.Utilisateur;
import com.ecommerce.ecommerce.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public Utilisateur inscription(Utilisateur utilisateur){
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        return utilisateurRepository.save(utilisateur);
    }
    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {

       return utilisateurRepository.findUtilisateurByEmail(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
       // return utilisateur.orElseThrow(()->new UsernameNotFoundException("user not found"));
    }
}
