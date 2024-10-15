package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.Role;
import com.ecommerce.ecommerce.entity.Utilisateur;
import com.ecommerce.ecommerce.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public Utilisateur inscription(Utilisateur utilisateur){
        Utilisateur  newUser = new Utilisateur();
        newUser.setUsername(utilisateur.getUsername());
        newUser.setEmail(utilisateur.getEmail());
        newUser.setRole(utilisateur.getRole());
        newUser.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        return utilisateurRepository.save(newUser);

    }
    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {

       return utilisateurRepository.findUtilisateurByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
       // return utilisateur.orElseThrow(()->new UsernameNotFoundException("user not found"));
    }
    public Optional<Utilisateur> getUtilisateur(String userId) {
        return utilisateurRepository.findById(userId);
    }
    public List<Utilisateur> getUtilisateursByRole(Role role) {
        return utilisateurRepository.findByRole(role);
    }
}
