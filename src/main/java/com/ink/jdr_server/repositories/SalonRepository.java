package com.ink.jdr_server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ink.jdr_server.entities.Salon;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {

    List<Salon> findByPartie_PartieId(Long partieId);

}