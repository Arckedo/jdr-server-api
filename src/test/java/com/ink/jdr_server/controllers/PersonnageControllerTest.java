package com.ink.jdr_server.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ink.jdr_server.dtos.personnage.PersonnageMapper;
import com.ink.jdr_server.dtos.personnage.PersonnageResponse;
import com.ink.jdr_server.entities.Personnage;
import com.ink.jdr_server.services.PersonnageService;

@WebMvcTest(PersonnageController.class)
public class PersonnageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonnageService personnageService;

    @MockitoBean
    private PersonnageMapper personnageMapper;

    private PersonnageResponse sampleResponse;


    @BeforeEach
    void setUp() {
        sampleResponse = new PersonnageResponse(1L, "Geralt de Riv", "Joueur_Pro");
    }

    @Test
    void testCreerPersonnage_Success() throws Exception {
        Long userId = 1L;     

        String jsonRequest = "{\"nom\": \"Geralt de Riv\"}";

        when(personnageService.creerPersonnage(eq(userId), any(String.class)))
            .thenReturn(new Personnage());
        when(personnageMapper.toDto(any(Personnage.class)))
            .thenReturn(sampleResponse);

        // WHEN & THEN
        mockMvc.perform(post("/api/personnages/utilisateur/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Geralt de Riv"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetMesPersonnages_Success() throws Exception {
        Long userId = 1L;

        when(personnageService.getPersonnagesParUtilisateur(userId))
            .thenReturn(List.of(new Personnage()));
        when(personnageMapper.toDtoList(any()))
            .thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/personnages/utilisateur/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom").value("Geralt de Riv"));
    }

    @Test
    void testGetById_Success() throws Exception {
        Long id = 1L;

        when(personnageService.getPersonnageById(id))
            .thenReturn(new Personnage());
        when(personnageMapper.toDto(any(Personnage.class)))
            .thenReturn(sampleResponse);

        mockMvc.perform(get("/api/personnages/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Geralt de Riv"));
    }

    @Test
    void testSupprimer_Success() throws Exception {
        Long id = 1L;
        Long userId = 1L;

        // doNothing() car la méthode du service renvoie void
        doNothing().when(personnageService).supprimerPersonnage(id, userId);

        mockMvc.perform(delete("/api/personnages/{id}/utilisateur/{userId}", id, userId))
                .andExpect(status().isNoContent());
    }
}