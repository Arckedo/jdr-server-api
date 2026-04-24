    package com.ink.jdr_server.dtos.message;

    public record MessageRequest (
        Long personnageId,
        String contenu
    ) {}