package com.example.work.controller;

import com.example.work.dto.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("/message")
public class MessageController {

    private List<Message> messages = new ArrayList<>(Arrays.asList(
            new Message(1, "Дом", "Место где живут люди", LocalDateTime.now()),
            new Message(2, "Глобус", "Уменьшенная модель земли", LocalDateTime.now()),
            new Message(3, "Ручка", "Устройство для письма", LocalDateTime.now())
    ));
    @GetMapping
    public List<Message> getAllMessages() {
        return messages;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int id) {
        Optional<Message> message = messages.stream().filter(m -> m.getId() == id).findFirst();
        return message.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {
        messages.add(message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message updatedMessage) {
        int index = -1;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Message existingMessage = messages.get(index);
        existingMessage.setTitle(updatedMessage.getTitle());
        existingMessage.setText(updatedMessage.getText());
        existingMessage.setTime(LocalDateTime.now());

        return new ResponseEntity<>(existingMessage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id) {
        boolean removed = messages.removeIf(m -> m.getId() == id);
        return removed ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
