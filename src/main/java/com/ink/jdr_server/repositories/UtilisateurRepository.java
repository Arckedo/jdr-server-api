package com.ink.jdr_server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ink.jdr_server.entities.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByMail(String mail);
    Optional<Utilisateur> findByPseudo(String pseudo);
    boolean existsByPseudo(String pseudo);
    boolean existsByMail(String mail);

}