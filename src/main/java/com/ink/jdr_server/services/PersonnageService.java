package com.ink.jdr_server.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ink.jdr_server.entities.Personnage;
import com.ink.jdr_server.entities.Utilisateur;
import com.ink.jdr_server.repositories.PersonnageRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonnageService {
    
    private final PersonnageRepository personnageRepo;
    private final UtilisateurService userService;
    
    public PersonnageService(PersonnageRepository personnageRepo, UtilisateurService userService) {
        this.personnageRepo = personnageRepo;
        this.userService = userService;
    }
    
    @Transactional
    public Personnage creerPersonnage(Long userId, String nom) {
        Utilisateur proprietaire = userService.findById(userId);

        Personnage personnage = new Personnage();
        personnage.setNom(nom);
        personnage.setProprietaire(proprietaire);

        return personnageRepo.save(personnage);
    }

    public Personnage getPersonnageById(Long personnageId) {
        return personnageRepo.findById(personnageId)
            .orElseThrow(() -> new RuntimeException("Personnage introuvable"));
    }
    
    public List<Personnage> getPersonnagesParUtilisateur(Long userId) {
        return personnageRepo.findByProprietaire_UserId(userId);
    }

    @Transactional
    public void supprimerPersonnage(Long personnageId, Long userId) {
        Personnage perso = getPersonnageById(personnageId);
        
        if (!perso.getProprietaire().getUserId().equals(userId)) {
            throw new RuntimeException("Vous n'avez aucun droit pour supprimer ce personnage.");
        }

        personnageRepo.delete(perso);
    }

}
