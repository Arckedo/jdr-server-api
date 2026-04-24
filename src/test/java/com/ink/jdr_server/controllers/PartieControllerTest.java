package com.ink.jdr_server.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ink.jdr_server.dtos.partie.PartieMapper;
import com.ink.jdr_server.dtos.partie.PartieResponse;
import com.ink.jdr_server.dtos.partie.SalonResponse;
import com.ink.jdr_server.dtos.personnage.PersonnageMapper;
import com.ink.jdr_server.entities.Partie;
import com.ink.jdr_server.entities.Salon;
import com.ink.jdr_server.services.PartieService;

@WebMvcTest(PartieController.class)
public class PartieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PartieService partieService;

    @MockitoBean
    private PartieMapper partieMapper;

    @MockitoBean
    private PersonnageMapper personnageMapper;

    private PartieResponse samplePartie;
    private SalonResponse sampleSalon;

    @BeforeEach
    void setUp() {
        samplePartie = new PartieResponse(1L, "La Quête de l'Anneau", 3, 5);
        sampleSalon = new SalonResponse(10L, "Auberge du Poney Fringuant");
    }

    @Test
    void testCreerPartie_Success() throws Exception {
        String jsonRequest = """
                {
                    "nom": "La Quête de l'Anneau"
                }
                """;

        when(partieService.creerPartie(any(), eq(1L))).thenReturn(new Partie());
        when(partieMapper.toDto(any())).thenReturn(samplePartie);

        mockMvc.perform(post("/api/parties/creer/{mjId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("La Quête de l'Anneau"));
    }

    @Test
    void testInviterJoueur_Success() throws Exception {
        doNothing().when(partieService).inviterJoueur(1L, 2L);

        mockMvc.perform(post("/api/parties/{partieId}/inviter/{userId}", 1L, 2L))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSalons_Success() throws Exception {
        when(partieService.getSalons(1L)).thenReturn(List.of(new Salon()));
        when(partieMapper.toSalonDtoList(any())).thenReturn(List.of(sampleSalon));

        mockMvc.perform(get("/api/parties/{partieId}/salons", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom").value("Auberge du Poney Fringuant"));
    }

    @Test
    void testAjouterSalon_Success() throws Exception {
        String jsonRequest = """
                {
                    "nom": "Forêt Noire"
                }
                """;

        when(partieService.ajouterSalon(eq(1L), any())).thenReturn(new Salon());
        when(partieMapper.toSalonDto(any())).thenReturn(sampleSalon);

        mockMvc.perform(post("/api/parties/{partieId}/salons", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testBannirJoueur_Success() throws Exception {
        doNothing().when(partieService).bannirJoueur(1L, 5L);

        mockMvc.perform(patch("/api/parties/{partieId}/bannir/{userId}", 1L, 5L))
                .andExpect(status().isOk());
    }

    @Test
    void testRetirerPersonnageDuSalon_Success() throws Exception {
        doNothing().when(partieService).retirerPersonnageDuSalon(10L, 100L);

        mockMvc.perform(delete("/api/parties/salons/{salonId}/personnages/{personnageId}", 10L, 100L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSupprimerPartie_Success() throws Exception {
        doNothing().when(partieService).supprimerPartie(1L);

        mockMvc.perform(delete("/api/parties/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAccepterInvitation_Success() throws Exception {
        doNothing().when(partieService).accepterInvitation(50L);

        mockMvc.perform(patch("/api/parties/invitation/{membreId}/accepter", 50L))
                .andExpect(status().isOk());
    }

    @Test
    void testSupprimerSalon_Success() throws Exception {
        doNothing().when(partieService).supprimerSalon(10L);

        mockMvc.perform(delete("/api/parties/salons/{salonId}", 10L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetPersonnagesDeLaPartie_Success() throws Exception {
        var persoResp = new com.ink.jdr_server.dtos.personnage.PersonnageResponse(1L, "Legolas", "Pseudo_Joueur");

        when(partieService.getPersonnagesDeLaPartie(1L)).thenReturn(List.of(new com.ink.jdr_server.entities.Personnage()));
        when(personnageMapper.toDtoList(any())).thenReturn(List.of(persoResp));

        mockMvc.perform(get("/api/parties/{partieId}/personnages", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom").value("Legolas"));
    }

    @Test
    void testAjouterPersonnageAuSalon_Success() throws Exception {
        doNothing().when(partieService).ajouterPersonnageAuSalon(10L, 1L);

        mockMvc.perform(post("/api/parties/salons/{salonId}/personnages/{personnageId}", 10L, 1L))
                .andExpect(status().isOk());
    }
}
