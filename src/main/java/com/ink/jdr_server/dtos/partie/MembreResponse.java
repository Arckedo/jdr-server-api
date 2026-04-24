package com.ink.jdr_server.dtos.partie;

public record MembreResponse(
    Long id, 
    String pseudo, 
    String role, 
    String statut
) {}