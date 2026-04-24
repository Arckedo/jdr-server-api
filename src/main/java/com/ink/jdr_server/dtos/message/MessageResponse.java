package com.ink.jdr_server.dtos.message;

import java.time.LocalDateTime;

public record MessageResponse(
    Long id,
    String contenu,
    String expediteurPseudo,
    String personnageNom,
    LocalDateTime dateEnvoi
) {}