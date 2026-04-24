package com.ink.jdr_server.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ink.jdr_server.dtos.utilisateur.UtilisateurMapper;
import com.ink.jdr_server.dtos.utilisateur.UtilisateurResponseDTO;
import com.ink.jdr_server.entities.Utilisateur;
import com.ink.jdr_server.services.UtilisateurService;

@WebMvcTest(UtilisateurController.class)
public class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurService userService;

    @MockitoBean
    private UtilisateurMapper userMapper;

    private UtilisateurResponseDTO sampleResponse;

    @BeforeEach
    void setUp() {
        sampleResponse = new UtilisateurResponseDTO(
            1L, 
            "Gandalf", 
            LocalDateTime.now(), 
            false
        );
    }
    @Test
    void testInscrire_Success() throws Exception {
        // On écrit le JSON manuellement avec un Text Block (""")
        String jsonRequest = """
                {
                    "pseudo": "Gandalf",
                    "mail": "magic@maia.com",
                    "password": "you-shall-not-pass"
                }
                """;

        when(userMapper.toEntity(any())).thenReturn(new Utilisateur());
        when(userService.inscrire(any())).thenReturn(new Utilisateur());
        when(userMapper.toDto(any())).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/utilisateur/inscrire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pseudo").value("Gandalf"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
        void testConnexion_Success() throws Exception {
            String jsonRequest = """
                    {
                        "mail": "magic@maia.com",
                        "password": "you-shall-not-pass"
                    }
                    """;

            when(userService.connexion(eq("magic@maia.com"), eq("you-shall-not-pass")))
                    .thenReturn(new Utilisateur());
            when(userMapper.toDto(any())).thenReturn(sampleResponse);

            mockMvc.perform(post("/api/utilisateur/connexion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pseudo").value("Gandalf"))
                    .andExpect(jsonPath("$.admin").value(false));
        }

    @Test
    void testFindById_Success() throws Exception {
        when(userService.findById(1L)).thenReturn(new Utilisateur());
        when(userMapper.toDto(any())).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/utilisateur/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pseudo").value("Gandalf"));
    }

    @Test
    void testSupprimerCompte_Success() throws Exception {
        doNothing().when(userService).supprimerUtilisateur(1L);

        mockMvc.perform(delete("/api/utilisateur/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testBannir_Success() throws Exception {
        when(userService.bannirUtilisateur(1L)).thenReturn(new Utilisateur());
        when(userMapper.toDto(any())).thenReturn(sampleResponse);

        mockMvc.perform(patch("/api/utilisateur/{id}/bannir", 1L))
                .andExpect(status().isOk());
    }
}