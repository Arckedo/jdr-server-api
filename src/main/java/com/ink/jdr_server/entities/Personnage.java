package com.ink.jdr_server.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="personnages")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Personnage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personnageId;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private Utilisateur proprietaire;

    @OneToMany(mappedBy = "personnage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @OneToMany(mappedBy = "personnage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalonPermission> permissionsSalons;

    @OneToMany(mappedBy = "personnage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembrePartie> participationsParties;
}
