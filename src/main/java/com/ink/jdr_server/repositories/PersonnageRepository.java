package com.ink.jdr_server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ink.jdr_server.entities.Personnage;

@Repository
public interface PersonnageRepository extends JpaRepository<Personnage, Long> {
    List<Personnage> findByProprietaire_UserId(Long userId);
}