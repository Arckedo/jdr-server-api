package com.ink.jdr_server.dtos.message;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ink.jdr_server.entities.Message;

@Component
public class MessageMapper {

    public MessageResponse toDto(Message entity) {
        if (entity == null) return null;

        return new MessageResponse(
            entity.getMessageId(),
            entity.getContenu(),
            entity.getExpediteur().getPseudo(),
            entity.getPersonnage() != null ? entity.getPersonnage().getNom() : null,
            entity.getDateEnvoi()
        );
    }

    public Message toEntity(MessageRequest request) {
        if (request == null) return null;

        Message message = new Message();
        message.setContenu(request.contenu());
        return message;
    }

    public List<MessageResponse> toDtoList(List<Message> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}