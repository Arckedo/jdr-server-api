package com.ink.jdr_server.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ink.jdr_server.dtos.message.MessageMapper;
import com.ink.jdr_server.dtos.message.MessageRequest;
import com.ink.jdr_server.dtos.message.MessageResponse;
import com.ink.jdr_server.entities.Message;
import com.ink.jdr_server.services.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @PostMapping("/salon/{salonId}/utilisateur/{userId}")
    public ResponseEntity<MessageResponse> envoyerMessage(
            @PathVariable Long salonId,
            @PathVariable Long userId,
            @RequestBody MessageRequest request) {
        
        Message message = messageService.envoyerMessage(salonId, userId, request);
        return ResponseEntity.ok(messageMapper.toDto(message));
    }

    @GetMapping("/salon/{salonId}/utilisateur/{userId}")
    public ResponseEntity<List<MessageResponse>> getHistorique(
            @PathVariable Long salonId,
            @PathVariable Long userId,
            @RequestParam(required = false) Long personnageId) {
        
        List<Message> messages = messageService.getMessagesParSalon(salonId, userId, personnageId);
        return ResponseEntity.ok(messageMapper.toDtoList(messages));
    }
}