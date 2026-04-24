package com.ink.jdr_server.services;

import org.springframework.stereotype.Service;

import com.ink.jdr_server.dtos.utilisateur.UserUpdateRequest;
import com.ink.jdr_server.entities.Utilisateur;
import com.ink.jdr_server.repositories.UtilisateurRepository;

import jakarta.transaction.Transactional;

@Service
public class UtilisateurService {
    
    private UtilisateurRepository userRepo;

    public UtilisateurService(UtilisateurRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public Utilisateur inscrire(Utilisateur user) {
        if (userRepo.existsByPseudo(user.getPseudo())) {
            throw new RuntimeException("Ce pseudo est déjà utilisé");
        } 
        if (userRepo.existsByMail(user.getMail())) {
            throw new RuntimeException("Ce Mail est déjà utilisé");
        }
        user.setIsAdmin(false);
        userRepo.save(user);
        return user;
    }

    @Transactional
    public Utilisateur connexion(String mail,String password){
        
        Utilisateur userEnBase = userRepo.findByMail(mail)
            .orElseThrow(() -> new RuntimeException("Identifiants incorrects"));
        
        if (Boolean.TRUE.equals(userEnBase.getIsBanned())) {
           throw new RuntimeException("Ce compte a été banni");
    }

        if (userEnBase.getPassword() != null && userEnBase.getPassword().equals(password)){
            return userEnBase;
        } else {
            throw new RuntimeException("Identifiants incorrects");
        }
    }

    @Transactional
    public Utilisateur modifierProfil(Long userid, UserUpdateRequest request ) {
        Utilisateur userEnBase = userRepo.findById(userid)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (request.pseudo() != null && !request.pseudo().isBlank()) {
            userEnBase.setPseudo(request.pseudo());
        }
        if (request.mail() != null && !request.mail().isBlank()) {    
            userEnBase.setMail(request.mail());
        }
        
        return userEnBase;
        
    }

    @Transactional
    public Utilisateur modifierProfil(Long userid, String password ) {
        Utilisateur userEnBase = userRepo.findById(userid)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        userEnBase.setPassword(password);
        return userEnBase;
    }
    
    @Transactional
    public void supprimerUtilisateur(Long userid) {
        if (! userRepo.existsById(userid)){
            throw new RuntimeException("Utilisateur non trouvé");
        }
        userRepo.deleteById(userid);
    }

    @Transactional
    public Utilisateur findByPseudo(String pseudo) {
        Utilisateur userEnBase = userRepo.findByPseudo(pseudo)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return userEnBase;
    }

    @Transactional
    public Utilisateur findById(Long id) {
        Utilisateur userEnBase = userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return userEnBase;
    }

    @Transactional
    public Utilisateur nommerAdmin(Long userId) {
        Utilisateur userEnBase = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        userEnBase.setIsAdmin(true);
        return userEnBase;
    }

    @Transactional
    public Utilisateur bannirUtilisateur(Long userId) {
    Utilisateur userEnBase = userRepo.findById(userId)
        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    
    userEnBase.setIsBanned(true);
    return userEnBase;
}
}
