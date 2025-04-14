package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    // ========== 1. User Registration ==========
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        try {
            Account created = accountService.register(account);
            if (created.getAccountId() == -1) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Duplicate username
            }
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // ========== 2. User Login ==========
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account loginAttempt) {
        Account account = accountService.login(loginAttempt.getUsername(), loginAttempt.getPassword());
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // ========== 3. Create Message ==========
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        Message created = messageService.createMessage(message);
        if (created != null) {
            return ResponseEntity.ok(created);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // ========== 4. Get All Messages ==========
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // ========== 5. Get Message by ID ==========
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        return messageService.getMessageById(messageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build()); // 200 with empty body if not found
    }
    

    // ========== 6. Delete Message by ID ==========
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int messageId) {
        int rowsAffected = messageService.deleteMessageById(messageId);
        if (rowsAffected == 0) {
            return ResponseEntity.ok().build(); // empty body but 200 status
        }
        return ResponseEntity.ok(rowsAffected);
    }

    // ========== 7. Update Message Text ==========
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int messageId, @RequestBody Message updatedTextOnly) {
        int rowsUpdated = messageService.updateMessageText(messageId, updatedTextOnly.getMessageText());
        if (rowsUpdated == 1) {
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // ========== 8. Get All Messages from Specific User ==========
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByAccountId(@PathVariable int accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }
}
