package com.ink.jdr_server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ink.jdr_server.entities.Personnage;
import com.ink.jdr_server.entities.Salon;
import com.ink.jdr_server.entities.SalonPermission;

@Repository
public interface SalonPermissionRepository extends JpaRepository<SalonPermission, Long> {

    boolean existsBySalonAndPersonnage(Salon salon, Personnage perso);

    void deleteBySalon_SalonIdAndPersonnage_PersonnageId(Long salonId, Long personnageId);

    void deleteBySalon(Salon salon);

    Optional<SalonPermission> findBySalonAndPersonnage(Salon salon, Personnage perso);

    Optional<SalonPermission> findBySalonAndPersonnage_PersonnageId(Salon salon, Long personnageId);

    boolean existsBySalonAndPersonnage_PersonnageId(Salon salon, Long personnageId);

}