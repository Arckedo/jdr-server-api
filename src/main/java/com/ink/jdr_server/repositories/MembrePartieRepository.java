package com.ink.jdr_server.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ink.jdr_server.entities.MembrePartie;
import com.ink.jdr_server.entities.Partie;
import com.ink.jdr_server.entities.Utilisateur;
import com.ink.jdr_server.entities.Enums.EnumMembrePartieStatut;

@Repository
public interface MembrePartieRepository extends JpaRepository<MembrePartie, Long> {

    Optional<MembrePartie> findByPartieAndUser(Partie partie, Utilisateur user);

    Optional<MembrePartie> findByPartie_PartieIdAndUser_UserId(Long partieId, Long userId);

    List<MembrePartie> findByPartie_PartieIdAndStatut(Long partieId, EnumMembrePartieStatut actif);

}