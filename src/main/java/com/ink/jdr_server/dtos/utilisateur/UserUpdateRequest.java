package com.ink.jdr_server.dtos.utilisateur;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

    @Size(min= 2, max=20, message = "Le pseudo doit avoir une taille entre 2 et 30 caractères")
    String pseudo,

    @Email(message = "L'email est invalide")
    String mail
) {}