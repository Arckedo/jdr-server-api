package com.ink.jdr_server.dtos.personnage;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ink.jdr_server.entities.Personnage;

@Component
public class PersonnageMapper {

    public PersonnageResponse toDto(Personnage entity) {
        if (entity == null) return null;

        return new PersonnageResponse(
            entity.getPersonnageId(),
            entity.getNom(),
            entity.getProprietaire().getPseudo()
        );
    }

    public Personnage toEntity(PersonnageRequest request) {
        if (request == null) return null;

        Personnage personnage = new Personnage();
        personnage.setNom(request.nom());
        // Le propriétaire sera lié dans le Service
        return personnage;
    }

    public List<PersonnageResponse> toDtoList(List<Personnage> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
