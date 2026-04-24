package com.ink.jdr_server.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ink.jdr_server.dtos.message.MessageRequest;
import com.ink.jdr_server.entities.Message;
import com.ink.jdr_server.entities.Personnage;
import com.ink.jdr_server.entities.Salon;
import com.ink.jdr_server.entities.SalonPermission;
import com.ink.jdr_server.entities.Utilisateur;
import com.ink.jdr_server.entities.Enums.EnumMembrePartieRole;
import com.ink.jdr_server.entities.Enums.EnumSalonPermission;
import com.ink.jdr_server.repositories.MembrePartieRepository;
import com.ink.jdr_server.repositories.MessageRepository;
import com.ink.jdr_server.repositories.SalonPermissionRepository;
import com.ink.jdr_server.repositories.SalonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepo;
    private final SalonRepository salonRepo;
    private final MembrePartieRepository membreRepo;
    private final SalonPermissionRepository permissionRepo;
    private final UtilisateurService userService;
    private final PersonnageService personnageService;

    @Transactional
    public Message envoyerMessage(Long salonId, Long userId, MessageRequest request) {
        Salon salon = salonRepo.findById(salonId)
            .orElseThrow(() -> new RuntimeException("Salon introuvable"));

        verifierDroitsEcriture(salon, userId, request.personnageId());

        Utilisateur expediteur = userService.findById(userId);
        Message message = new Message();
        message.setSalon(salon);
        message.setExpediteur(expediteur);

        if (request.personnageId() != null) {
            Personnage perso = personnageService.getPersonnageById(request.personnageId());
            if (!perso.getProprietaire().getUserId().equals(userId)) {
                throw new RuntimeException("Ce n'est pas votre personnage !");
            }
            message.setPersonnage(perso);
        }
        String contenu = request.contenu(); 
        
        if (contenu.startsWith("/roll")) {
            contenu = traiterJetDeDes(contenu);
        }

        message.setContenu(contenu);
        return messageRepo.save(message);
    }

    private void verifierDroitsEcriture(Salon salon, Long userId, Long personnageId) {
        boolean isMJ = membreRepo.findByPartie_PartieIdAndUser_UserId(salon.getPartie().getPartieId(), userId)
            .map(m -> m.getRole() == EnumMembrePartieRole.MJ)
            .orElse(false);

        if (isMJ) return;

        if (personnageId == null) {
            throw new RuntimeException("Action impossible : aucun personnage sélectionné.");
        }

        SalonPermission perm = permissionRepo.findBySalonAndPersonnage_PersonnageId(salon, personnageId)
            .orElseThrow(() -> new RuntimeException("Accès refusé à ce salon."));

        if (perm.getPermission() == EnumSalonPermission.LECTURE) {
            throw new RuntimeException("Ce salon est en lecture seule.");
        }
    }

    private String traiterJetDeDes(String commande) {
        int resultat = (int) (Math.random() * 20) + 1;
        return "**Jet de dé (d20) :** " + resultat;
    }

    public List<Message> getMessagesParSalon(Long salonId, Long userId, Long personnageId) {
        Salon salon = salonRepo.findById(salonId)
            .orElseThrow(() -> new RuntimeException("Salon introuvable"));

        boolean isMJ = membreRepo.findByPartie_PartieIdAndUser_UserId(salon.getPartie().getPartieId(), userId)
            .map(m -> m.getRole() == EnumMembrePartieRole.MJ)
            .orElse(false);

        if (!isMJ) {
            if (personnageId == null || !permissionRepo.existsBySalonAndPersonnage_PersonnageId(salon, personnageId)) {
                throw new RuntimeException("Accès à l'historique refusé.");
            }
        }

        return messageRepo.findBySalon_SalonIdOrderByDateEnvoiAsc(salonId);
    }
}