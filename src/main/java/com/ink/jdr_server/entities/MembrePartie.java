package com.ink.jdr_server.entities;

import java.time.LocalDateTime;

import com.ink.jdr_server.entities.Enums.EnumMembrePartieRole;
import com.ink.jdr_server.entities.Enums.EnumMembrePartieStatut;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="membres_partie",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"partie_id", "user_id"})
    })
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MembrePartie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membrePartieId;

    @ManyToOne
    @JoinColumn(name="partie_id", nullable = false)
    private Partie partie;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Utilisateur user;

    @ManyToOne
    @JoinColumn(name="personnage_id")
    private Personnage personnage;

    @Enumerated(EnumType.STRING)
    private EnumMembrePartieRole role; 

    @Enumerated(EnumType.STRING)
    private EnumMembrePartieStatut statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateRejoint = LocalDateTime.now();
}
