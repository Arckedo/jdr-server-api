package com.ink.jdr_server.dtos.partie;

public record PartieResponse(
    Long id, 
    String nom, 
    Integer nombreJoueur, 
    Integer maxJoueurs
) {}