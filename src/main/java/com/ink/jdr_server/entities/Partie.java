package com.ink.jdr_server.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="parties")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Partie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partieId;

    private Integer nombreJoueur = 0;

    private Integer maxJoueurs;

    private String nom;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateDeCreation = LocalDateTime.now();

    @OneToMany(mappedBy = "partie")
    private List<Salon> salons;
}
