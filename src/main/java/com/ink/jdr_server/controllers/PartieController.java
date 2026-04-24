package com.ink.jdr_server.controllers;

import java.util.List;

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

import com.ink.jdr_server.dtos.partie.PartieMapper;
import com.ink.jdr_server.dtos.partie.PartieRequest;
import com.ink.jdr_server.dtos.partie.PartieResponse;
import com.ink.jdr_server.dtos.partie.PermissionRequest;
import com.ink.jdr_server.dtos.partie.SalonRequest;
import com.ink.jdr_server.dtos.partie.SalonResponse;
import com.ink.jdr_server.dtos.personnage.PersonnageMapper;
import com.ink.jdr_server.dtos.personnage.PersonnageResponse;
import com.ink.jdr_server.entities.Partie;
import com.ink.jdr_server.entities.Personnage;
import com.ink.jdr_server.entities.Salon;
import com.ink.jdr_server.services.PartieService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parties")
@RequiredArgsConstructor
public class PartieController {

    private final PersonnageMapper personnageMapper;
    private final PartieService partieService;
    private final PartieMapper partieMapper;


    @PostMapping("/creer/{mjId}")
    public ResponseEntity<PartieResponse> creerPartie(@PathVariable Long mjId, @Valid @RequestBody PartieRequest request) {
        Partie partie = partieService.creerPartie(request, mjId);
        return ResponseEntity.status(HttpStatus.CREATED).body(partieMapper.toDto(partie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPartie(@PathVariable Long id) {
        partieService.supprimerPartie(id);
        return ResponseEntity.noContent().build();
    }

    




    @PostMapping("/{partieId}/inviter/{userId}")
    public ResponseEntity<Void> inviterJoueur(@PathVariable Long partieId, @PathVariable Long userId) {
        partieService.inviterJoueur(partieId, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/invitation/{membreId}/accepter")
    public ResponseEntity<Void> accepterInvitation(@PathVariable Long membreId) {
        partieService.accepterInvitation(membreId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{partieId}/bannir/{userId}")
    public ResponseEntity<Void> bannirJoueur(@PathVariable Long partieId, @PathVariable Long userId) {
        partieService.bannirJoueur(partieId, userId);
        return ResponseEntity.ok().build();
    }

    




    @GetMapping("/{partieId}/salons")
    public ResponseEntity<List<SalonResponse>> getSalons(@PathVariable Long partieId) {
        List<Salon> salons = partieService.getSalons(partieId);
        return ResponseEntity.ok(partieMapper.toSalonDtoList(salons));
    }

    @PostMapping("/{partieId}/salons")
    public ResponseEntity<SalonResponse> ajouterSalon(@PathVariable Long partieId, @Valid @RequestBody SalonRequest request) {
        Salon salon = partieService.ajouterSalon(partieId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(partieMapper.toSalonDto(salon));
    }

    @DeleteMapping("/salons/{salonId}")
    public ResponseEntity<Void> supprimerSalon(@PathVariable Long salonId) {
        partieService.supprimerSalon(salonId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{partieId}/personnages")
    public ResponseEntity<List<PersonnageResponse>> getPersonnagesDeLaPartie(@PathVariable Long partieId) {
        // Note : On utilise le PersonnageMapper ici pour transformer les entités en DTO
        // Tu devras peut-être injecter PersonnageMapper dans ce controller
        List<Personnage> personnages = partieService.getPersonnagesDeLaPartie(partieId);
        return ResponseEntity.ok(personnageMapper.toDtoList(personnages));
    }

    @PostMapping("/salons/{salonId}/personnages/{personnageId}")
    public ResponseEntity<Void> ajouterPersonnageAuSalon(
            @PathVariable Long salonId, 
            @PathVariable Long personnageId) {
        partieService.ajouterPersonnageAuSalon(salonId, personnageId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/salons/{salonId}/personnages/{personnageId}")
    public ResponseEntity<Void> retirerPersonnageDuSalon(
            @PathVariable Long salonId, 
            @PathVariable Long personnageId) {
        partieService.retirerPersonnageDuSalon(salonId, personnageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/salons/{salonId}/permissions")
    public ResponseEntity<Void> modifierAcces(@PathVariable Long salonId, @Valid @RequestBody PermissionRequest request) {
        partieService.modifierAccesPersonnage(salonId, request);
        return ResponseEntity.ok().build();
    }


    
}