package com.ink.jdr_server.dtos.utilisateur;

import java.time.LocalDateTime;

public record UtilisateurResponseDTO(
    Long id,
    String pseudo,
    LocalDateTime dateDeCreation,
    boolean admin
) {}