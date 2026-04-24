package com.ink.jdr_server.dtos.partie;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ink.jdr_server.entities.MembrePartie;
import com.ink.jdr_server.entities.Partie;
import com.ink.jdr_server.entities.Salon;

@Component
public class PartieMapper {

    public PartieResponse toDto(Partie entity) {
        if (entity == null) return null;
        return new PartieResponse(
            entity.getPartieId(),
            entity.getNom(),
            entity.getNombreJoueur(),
            entity.getMaxJoueurs()
        );
    }

    public SalonResponse toSalonDto(Salon entity) {
        if (entity == null) return null;
        return new SalonResponse(entity.getSalonId(), entity.getNom());
    }

    public MembreResponse toMembreDto(MembrePartie entity) {
        if (entity == null) return null;
        return new MembreResponse(
            entity.getMembrePartieId(),
            entity.getUser().getPseudo(),
            entity.getRole().name(),
            entity.getStatut().name()
        );
    }

    public List<PartieResponse> toDtoList(List<Partie> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    public List<SalonResponse> toSalonDtoList(List<Salon> entities) {
        return entities.stream().map(this::toSalonDto).toList();
    }
}