package com.ink.jdr_server.entities;

import com.ink.jdr_server.entities.Enums.EnumSalonPermission;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="salon_permissions",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"salon_id", "personnage_id"})
    })
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SalonPermission {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name="salon_id", nullable = false)
    private Salon salon;


    @ManyToOne
    @JoinColumn(name="personnage_id", nullable = false)
    private Personnage personnage;

    @Enumerated(EnumType.STRING)
    private EnumSalonPermission permission;
}
