package com.ink.jdr_server.dtos.partie;

import com.ink.jdr_server.entities.Enums.EnumSalonPermission;
import jakarta.validation.constraints.NotNull;

public record PermissionRequest(
    @NotNull(message = "L'ID du personnage est requis")
    Long personnageId,
    
    EnumSalonPermission niveau
) {}