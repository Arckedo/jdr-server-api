package com.ink.jdr_server.dtos.partie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SalonRequest(
    @NotBlank(message = "Le nom du salon est obligatoire.")
    @Size(min = 2, max = 50, message = "Le nom doit avoir entre 2 et 50 caractères")
    String nom
) {}