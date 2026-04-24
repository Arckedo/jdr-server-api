package com.ink.jdr_server.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.ink.jdr_server.dtos.message.MessageMapper;
import com.ink.jdr_server.dtos.message.MessageRequest;
import com.ink.jdr_server.dtos.message.MessageResponse;
import com.ink.jdr_server.entities.Message;
import com.ink.jdr_server.services.MessageService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MessageService messageService;

    @MockitoBean
    private MessageMapper messageMapper;

    private MessageResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleResponse = new MessageResponse(
            1L, 
            "Je lance un sort de foudre ! /roll 1d20", 
            "Joueur_Pro", 
            "Eldarin",    
            LocalDateTime.now()
        );
    }

    @Test
    void testEnvoyerMessage_Success() throws Exception {
        // GIVEN
        Long salonId = 1L;
        Long userId = 2L;
        MessageRequest request = new MessageRequest(10L,"Je lance un sort de foudre ! /roll 1d20");
        
        when(messageService.envoyerMessage(eq(salonId), eq(userId), any(MessageRequest.class)))
            .thenReturn(new Message());
        when(messageMapper.toDto(any(Message.class)))
            .thenReturn(sampleResponse);

        // WHEN & THEN
        mockMvc.perform(post("/api/messages/salon/{salonId}/utilisateur/{userId}", salonId, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenu").value(sampleResponse.contenu()))
                .andExpect(jsonPath("$.personnageNom").value("Eldarin"));
    }

    @Test
    void testGetHistorique_Success() throws Exception {
        // GIVEN
        Long salonId = 1L;
        Long userId = 2L;
        Long personnageId = 10L;
        
        when(messageService.getMessagesParSalon(eq(salonId), eq(userId), eq(personnageId)))
            .thenReturn(List.of(new Message()));
        when(messageMapper.toDtoList(any()))
            .thenReturn(List.of(sampleResponse));

        // WHEN & THEN
        mockMvc.perform(get("/api/messages/salon/{salonId}/utilisateur/{userId}", salonId, userId)
                .param("personnageId", personnageId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contenu").value(sampleResponse.contenu()))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetHistorique_SansPersonnageId() throws Exception {
        // Test de la version facultative du paramètre personnageId
        Long salonId = 1L;
        Long userId = 2L;

        when(messageService.getMessagesParSalon(eq(salonId), eq(userId), eq(null)))
            .thenReturn(List.of(new Message()));
        when(messageMapper.toDtoList(any()))
            .thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/messages/salon/{salonId}/utilisateur/{userId}", salonId, userId))
                .andExpect(status().isOk());
    }
}