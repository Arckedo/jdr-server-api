package com.ink.jdr_server.dtos.utilisateur;

import org.springframework.stereotype.Component;

import com.ink.jdr_server.entities.Utilisateur;

@Component
public class UtilisateurMapper {

    public UtilisateurResponseDTO toDto(Utilisateur entity) {
        if (entity == null) return null;

        return new UtilisateurResponseDTO(
            entity.getUserId(),
            entity.getPseudo(),
            entity.getDateDeCreation(),
            entity.getIsAdmin()
        );
    }

    public Utilisateur toEntity(InscriptionRequest request) {
        if (request == null) return null;
        Utilisateur user = new Utilisateur();
        user.setPseudo(request.pseudo());
        user.setMail(request.mail());
        user.setPassword(request.password());
        return user;
    
    }
}