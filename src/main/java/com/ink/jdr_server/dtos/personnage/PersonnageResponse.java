package com.ink.jdr_server.dtos.personnage;

public record PersonnageResponse(
    Long id,
    String nom,
    String proprietairePseudo
) {}