package com.ink.jdr_server.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ink.jdr_server.dtos.personnage.PersonnageMapper;
import com.ink.jdr_server.dtos.personnage.PersonnageRequest;
import com.ink.jdr_server.dtos.personnage.PersonnageResponse;
import com.ink.jdr_server.entities.Personnage;
import com.ink.jdr_server.services.PersonnageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/personnages")
@RequiredArgsConstructor
public class PersonnageController {

    private final PersonnageService personnageService;
    private final PersonnageMapper personnageMapper;

    @PostMapping("/utilisateur/{userId}")
    public ResponseEntity<PersonnageResponse> creer(
            @PathVariable Long userId, 
            @RequestBody PersonnageRequest request) {
        
        Personnage perso = personnageService.creerPersonnage(userId, request.nom());
        return ResponseEntity.status(HttpStatus.CREATED).body(personnageMapper.toDto(perso));
    }

    @GetMapping("/utilisateur/{userId}")
    public ResponseEntity<List<PersonnageResponse>> getMesPersonnages(@PathVariable Long userId) {
        List<Personnage> persos = personnageService.getPersonnagesParUtilisateur(userId);
        return ResponseEntity.ok(personnageMapper.toDtoList(persos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonnageResponse> getById(@PathVariable Long id) {
        Personnage perso = personnageService.getPersonnageById(id);
        return ResponseEntity.ok(personnageMapper.toDto(perso));
    }

    @DeleteMapping("/{id}/utilisateur/{userId}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id, @PathVariable Long userId) {
        personnageService.supprimerPersonnage(id, userId);
        return ResponseEntity.noContent().build();
    }
}