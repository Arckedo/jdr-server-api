package com.ink.jdr_server.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utilisateurs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Le pseudo est obligatoire")
    @Size(min= 2, max=20, message = "Le pseudo doit avoir une taille entre 2 et 30 caractères")
    @Column(nullable = false, unique = true)
    private String pseudo;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email est invalide")
    @Column(nullable = false, unique = true)
    private String mail;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères")
    @Column(nullable = false)
    private String password;

    @Column(name="date_de_creation" ,nullable = false, updatable = false)
    private LocalDateTime dateDeCreation;

    @PrePersist
    protected void onCreate() {
        this.dateDeCreation = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Personnage> personnages;

    @OneToMany(mappedBy = "expediteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messagesEnvoyes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembrePartie> participations;
    private Boolean isAdmin = false;

    private Boolean isBanned = false;
    
}
