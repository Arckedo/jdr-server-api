package com.ink.jdr_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ink.jdr_server.entities.Partie;

@Repository
public interface PartieRepository extends JpaRepository<Partie, Long> {

}