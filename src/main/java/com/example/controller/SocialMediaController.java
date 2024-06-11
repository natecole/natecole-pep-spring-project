package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /*TODO write register controller method*/
    @PostMapping("/register")
    public ResponseEntity<Account> register() {
        return new ResponseEntity<>(null);
    }

    /*TODO write login controller method*/
    @PostMapping("/login")
    public ResponseEntity<Account> login() {
        return new ResponseEntity<>(null);
    }

    /*TODO write message creation controller method*/
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage() {
        return new ResponseEntity<>(null);
    }

    /*TODO write message retrieval controller method*/
    @GetMapping("/messages{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable int messageId) {
        return new ResponseEntity<>(null);
    }

    /*TODO write message deletion controller method*/
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int messageId) {
        return new ResponseEntity<>(null);
    }

    /*TODO write message update controller method*/
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int messageId) {
        return new ResponseEntity<>(null);
    }

    /*TODO write all message retrieval controller method*/
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessages(@PathVariable int accountId) {
        return new ResponseEntity<>(null);
    }
}
