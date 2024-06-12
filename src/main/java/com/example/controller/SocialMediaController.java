package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.RegistrationException;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

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

    /**
     * Controller handler for registering a new account.
     * Expects an {@link Account} object in the request body.
     * The username and password must not be blank and the password must be
     * at least 4 characters long.
     * 
     * @param account  The account to be created, without an accountId
     * @return The account with an accountId if successful
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {

        if (account.getUsername() == null || account.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password must not be empty");
        }

        try {
            Optional<Account> createdAccount = accountService.createAccount(account);
            return ResponseEntity.ok().body(createdAccount.get());
        } catch(DuplicateUsernameException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch(RegistrationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Controller handler for logging in an account.
     * Expects an {@link Account} object in the request body.
     * 
     * @param account The account to be logged in
     * @return The account if the login was successful
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {

        if (account.getUsername() == null || account.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password must not be empty");
        }

        Optional<Account> loggedInAccount = accountService.login(account);

        if(loggedInAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return ResponseEntity.ok().body(loggedInAccount.get());
        }
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
