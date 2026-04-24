package com.ink.jdr_server.dtos.utilisateur;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InscriptionRequest(
    @NotBlank(message = "Le pseudo est obligatoire")
    @Size(min = 2, max = 20)
    String pseudo,

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    String mail,

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8)
    String password
) {}