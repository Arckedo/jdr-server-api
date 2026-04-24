package com.ink.jdr_server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ink.jdr_server.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySalon_SalonIdOrderByDateEnvoiAsc(Long salonId);

}