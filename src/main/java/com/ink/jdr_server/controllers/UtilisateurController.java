package com.ink.jdr_server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ink.jdr_server.dtos.utilisateur.ConnexionRequest;
import com.ink.jdr_server.dtos.utilisateur.InscriptionRequest;
import com.ink.jdr_server.dtos.utilisateur.UserUpdateRequest;
import com.ink.jdr_server.dtos.utilisateur.UtilisateurMapper;
import com.ink.jdr_server.dtos.utilisateur.UtilisateurResponseDTO;
import com.ink.jdr_server.entities.Utilisateur;
import com.ink.jdr_server.services.UtilisateurService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/utilisateur")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurMapper userMapper;
    private final UtilisateurService userService;

    @PostMapping("/inscrire")
    public ResponseEntity<UtilisateurResponseDTO> inscrire(@Valid @RequestBody InscriptionRequest request) {
        Utilisateur entity = userMapper.toEntity(request);
        Utilisateur inscrit = userService.inscrire(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(inscrit));
    }
    
    @PostMapping("/connexion")
    public ResponseEntity<UtilisateurResponseDTO> connexion(@Valid @RequestBody ConnexionRequest request) {
        Utilisateur entity = userService.connexion(request.mail(), request.password());
        return ResponseEntity.ok(userMapper.toDto(entity));
    }

    @GetMapping("/pseudo/{pseudo}")
    public ResponseEntity<UtilisateurResponseDTO> findByPseudo(@PathVariable String pseudo) {
        Utilisateur entity = userService.findByPseudo(pseudo);
        return ResponseEntity.ok(userMapper.toDto(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> findById(@PathVariable Long id) {
        Utilisateur entity = userService.findById(id);
        return ResponseEntity.ok(userMapper.toDto(entity));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> modifier(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        Utilisateur entity = userService.modifierProfil(id, request);
        return ResponseEntity.ok(userMapper.toDto(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCompte(@PathVariable Long id) {
        userService.supprimerUtilisateur(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/bannir")
    public ResponseEntity<UtilisateurResponseDTO> bannir(@PathVariable Long id) {
        Utilisateur entity = userService.bannirUtilisateur(id);
        return ResponseEntity.ok(userMapper.toDto(entity));
    }

    @PatchMapping("/{id}/nommer-admin")
    public ResponseEntity<UtilisateurResponseDTO> nommerAdmin(@PathVariable Long id) {
        Utilisateur entity = userService.nommerAdmin(id);
        return ResponseEntity.ok(userMapper.toDto(entity));
    }
}