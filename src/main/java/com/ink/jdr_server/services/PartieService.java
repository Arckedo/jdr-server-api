package com.ink.jdr_server.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ink.jdr_server.dtos.partie.PartieRequest;
import com.ink.jdr_server.dtos.partie.PermissionRequest;
import com.ink.jdr_server.dtos.partie.SalonRequest;
import com.ink.jdr_server.entities.MembrePartie;
import com.ink.jdr_server.entities.Partie;
import com.ink.jdr_server.entities.Personnage;
import com.ink.jdr_server.entities.Salon;
import com.ink.jdr_server.entities.SalonPermission;
import com.ink.jdr_server.entities.Utilisateur;
import com.ink.jdr_server.entities.Enums.EnumMembrePartieRole;
import com.ink.jdr_server.entities.Enums.EnumMembrePartieStatut;
import com.ink.jdr_server.entities.Enums.EnumSalonPermission;
import com.ink.jdr_server.repositories.MembrePartieRepository;
import com.ink.jdr_server.repositories.PartieRepository;
import com.ink.jdr_server.repositories.PersonnageRepository;
import com.ink.jdr_server.repositories.SalonPermissionRepository;
import com.ink.jdr_server.repositories.SalonRepository;

import jakarta.transaction.Transactional;

@Service
public class PartieService {
    
    private final PartieRepository partieRepo;
    private final MembrePartieRepository membreRepo;
    private final SalonRepository salonRepo;
    private final PersonnageRepository personnageRepo;
    private final SalonPermissionRepository permissionRepo;
    private final UtilisateurService userService;
    
    
    public PartieService(PartieRepository partieRepo, MembrePartieRepository membreRepo, SalonRepository salonRepo,
           PersonnageRepository personnageRepo, SalonPermissionRepository permissionRepo, 
           UtilisateurService userService) {
        this.partieRepo = partieRepo;
        this.membreRepo = membreRepo;
        this.salonRepo = salonRepo;
        this.personnageRepo = personnageRepo;
        this.userService = userService;
        this.permissionRepo = permissionRepo;
    }

    @Transactional
    public Partie creerPartie(PartieRequest request, Long mjId) {
        Utilisateur mj = userService.findById(mjId);

        Partie partie = new Partie();
        partie.setNom(request.nom());
        partie.setMaxJoueurs(request.maxJoueurs() != null ? request.maxJoueurs() : 5);
        partie.setNombreJoueur(1);
        partie = partieRepo.save(partie);

        MembrePartie membreMj = new MembrePartie();
        membreMj.setPartie(partie);
        membreMj.setUser(mj);
        membreMj.setRole(EnumMembrePartieRole.MJ);
        membreMj.setStatut(EnumMembrePartieStatut.ACTIF);
        membreRepo.save(membreMj);

        ajouterSalon(partie.getPartieId(), new SalonRequest("Général"));

        return partie;
    }
    
    @Transactional
    public void inviterJoueur(Long partieId, Long userId) {
        Partie partie = partieRepo.findById(partieId)
            .orElseThrow(() -> new RuntimeException("Partie non trouvée"));
        Utilisateur user = userService.findById(userId);

        Optional<MembrePartie> membreOpt = membreRepo.findByPartieAndUser(partie, user);

        if (membreOpt.isPresent()) {
            MembrePartie membreExistant = membreOpt.get();
            EnumMembrePartieStatut statut = membreExistant.getStatut();

            switch (statut) {
                case ACTIF -> 
                    throw new RuntimeException("Le joueur est déjà dans la partie !");
                case INVITE -> 
                    throw new RuntimeException("Une invitation est déjà en attente pour ce joueur.");
                case SPECTATEUR -> 
                    throw new RuntimeException("Le joueur est spectateur dans la partie !");
                case BANNI -> 
                    throw new RuntimeException("Ce joueur est banni de cette partie.");
                case QUITTE -> {
                    membreExistant.setStatut(EnumMembrePartieStatut.INVITE);
                    return; 
                }
            }
        }

        MembrePartie invitation = new MembrePartie();
        invitation.setPartie(partie);
        invitation.setUser(user);
        invitation.setRole(EnumMembrePartieRole.JOUEUR);
        invitation.setStatut(EnumMembrePartieStatut.INVITE);
        membreRepo.save(invitation);
    }

    @Transactional
    public void accepterInvitation(Long membrePartieId) {
        MembrePartie membre = membreRepo.findById(membrePartieId)
            .orElseThrow(() -> new RuntimeException("Invitation non trouvée"));

        Partie partie = membre.getPartie();
        if (partie.getNombreJoueur() >= partie.getMaxJoueurs()) {
            throw new RuntimeException("La partie est complète");
        }

        membre.setStatut(EnumMembrePartieStatut.ACTIF);
        partie.setNombreJoueur(partie.getNombreJoueur() + 1);
    }

    @Transactional
    public void bannirJoueur(Long partieId, Long userId) {

        MembrePartie membre = membreRepo.findByPartie_PartieIdAndUser_UserId(partieId, userId)
            .orElseThrow(() -> new RuntimeException("Ce joueur ne fait pas partie de cette aventure."));

        if (EnumMembrePartieStatut.ACTIF.equals(membre.getStatut())) {
            Partie partie = membre.getPartie();
            partie.setNombreJoueur(Math.max(0, partie.getNombreJoueur() - 1));
        }

        membre.setStatut(EnumMembrePartieStatut.BANNI);
    }

    @Transactional
    public Salon ajouterSalon(Long partieId, SalonRequest request) {
        Partie partie = partieRepo.findById(partieId)
            .orElseThrow(() -> new RuntimeException("Partie non trouvée"));
            
        Salon salon = new Salon();
        salon.setNom(request.nom());
        salon.setPartie(partie);
        return salonRepo.save(salon);
    }

    @Transactional
    public void supprimerPartie(Long partieId) {
        if (!partieRepo.existsById(partieId)) {
            throw new RuntimeException("Partie non trouvée");
        }
        partieRepo.deleteById(partieId);
    }

    @Transactional
    public void supprimerSalon(Long salonId) {

        if (!salonRepo.existsById(salonId)) {
            throw new RuntimeException("Salon introuvable");
        }
        salonRepo.deleteById(salonId);
    }

    @Transactional
    public void ajouterPersonnageAuSalon(Long salonId, Long personnageId) {
        Salon salon = salonRepo.findById(salonId).orElseThrow();
        Personnage perso = personnageRepo.findById(personnageId).orElseThrow();

        if (!permissionRepo.existsBySalonAndPersonnage(salon, perso)) {
            SalonPermission permission = new SalonPermission();
            permission.setSalon(salon);
            permission.setPersonnage(perso);
            permission.setPermission(EnumSalonPermission.ECRITURE);
            permissionRepo.save(permission);
        }
    }

    @Transactional
    public void retirerPersonnageDuSalon(Long salonId, Long personnageId) {
        permissionRepo.deleteBySalon_SalonIdAndPersonnage_PersonnageId(salonId, personnageId);
    }

    @Transactional
    public void modifierAccesPersonnage(Long salonId, PermissionRequest request) {
        Salon salon = salonRepo.findById(salonId)
            .orElseThrow(() -> new RuntimeException("Salon introuvable"));
        Personnage perso = personnageRepo.findById(request.personnageId())
            .orElseThrow(() -> new RuntimeException("Personnage introuvable"));

        Optional<SalonPermission> permissionOpt = permissionRepo.findBySalonAndPersonnage(salon, perso);

        if (request.niveau() == null) {
            permissionOpt.ifPresent(permissionRepo::delete);
        } else {
            SalonPermission perm = permissionOpt.orElse(new SalonPermission());
            perm.setSalon(salon);
            perm.setPersonnage(perso);
            perm.setPermission(request.niveau());
            
            permissionRepo.save(perm);
        }
    }

    public List<Salon> getSalons(Long partieId) {
        return salonRepo.findByPartie_PartieId(partieId);
    }

    public List<Personnage> getPersonnagesDeLaPartie(Long partieId) {
        List<MembrePartie> membresActifs = membreRepo.findByPartie_PartieIdAndStatut(
            partieId, 
            EnumMembrePartieStatut.ACTIF
        );

        return membresActifs.stream()
                .map(MembrePartie::getPersonnage)
                .filter(Objects::nonNull)
                .toList();
    }
}
