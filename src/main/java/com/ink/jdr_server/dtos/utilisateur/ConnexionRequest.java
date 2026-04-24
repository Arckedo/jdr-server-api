package com.ink.jdr_server.dtos.utilisateur;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ConnexionRequest(
    @NotBlank @Email String mail,
    @NotBlank String password
) {}