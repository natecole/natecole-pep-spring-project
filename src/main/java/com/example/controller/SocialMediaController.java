package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.MessageNotFoundException;
import com.example.exception.RegistrationException;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;
import java.util.Optional;

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
     * Expects an {@link Account} in the request body.
     * The username and password must not be blank and the password must be
     * at least 4 characters long.
     * If the {@link AccountService} finds a duplicate username, the API will return a 409.
     * If the {@link AccountService} encounters an error while registering, the API will return a 400
     * 
     * @param account  The account to be created, without an accountId
     * @return The account with an accountId if registered successfully
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {

        if (account.getUsername().isEmpty() || account.getPassword().isEmpty()) {
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
     * Expects an {@link Account} in the request body.
     * 
     * @param account The account to be logged in
     * @return The account if the login was successful
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {

        if (account.getUsername() == null || account.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body("Username and password must not be empty");
        }

        Optional<Account> loggedInAccount = accountService.login(account);

        if(loggedInAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        } else {
            return ResponseEntity.ok()
                    .body(loggedInAccount.get());
        }
    }

    /**
     * Controller handler for creating a message.
     * Expects a {@link Message} in the request body.
     * The message text must be between 1 and 255 characters long
     * and the account posting must exist in the database.
     * 
     * @param message  The message to be created, without a messageId
     * @return The message with an messageId if created successfully
     */
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {

        if (!accountService.accountExists(message.getPostedBy())) {
            return ResponseEntity.badRequest()
                    .body("The account posting this message does not exist");
        }

        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 255) {
            return ResponseEntity.badRequest()
                    .body("The message text must be between 1 and 255 characters long");
        }

        return ResponseEntity.ok()
                .body(messageService.createMessage(message));
    }

    /**
     * Controller handler for retrieving all messages in the database
     * 
     * @return A list of all messages
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessage() {        
        return ResponseEntity.ok()
                .body(messageService.getAllMessages());
    }

    /**
     * Controller handler for retrieving a {@link Message} by its messageId
     * 
     * @param messageId
     * @return The message if it exists
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable int messageId) {
        Optional<Message> message = messageService.getMessage(messageId);

        if (message.isEmpty()) {
            return ResponseEntity.ok()
                    .build();
        } else {
            return ResponseEntity.ok()
                    .body(message.get());
        }
    }

    /**
     * Controller handler for deleting a {@link Message} by its messageId.
     * If the {@link MessageService} cannot find the message, the API will return a 400.
     * 
     * @param messageId
     * @return The number of rows affected if successful
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int messageId) {
        try{
            int rowsAffected = messageService.deleteMessage(messageId);
            return ResponseEntity.ok()
                    .body(rowsAffected);
        } catch(MessageNotFoundException ex) {
            return ResponseEntity.ok()
                        .build();
        }
    }

    /**
     * Controller handler for updating a {@link Message} by is messageId given new message text.
     * Expects a String in the request body.
     * The new message text must be between 1 and 255 characters long and
     * the message must already exist.
     * If the {@link MessageService} cannot find the message, the API will return a 400.
     * 
     * @param messageId
     * @param messageText
     * @return The number of rows affected if successful
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable int messageId, @RequestBody Message message) {

        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 255) {
            return ResponseEntity.badRequest()
                    .body("The message text must be between 1 and 255 characters long");
        }

        try {
            int rowsAffected = messageService.updateMessage(messageId, message.getMessageText());
            return ResponseEntity.ok()
                    .body(rowsAffected);
        } catch (MessageNotFoundException ex) {
            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }

    /**
     * Controller handler for retrieving all messages for an {@link Account} given an id.
     * 
     * @param accountId
     * @return A list of all messages posted by the account
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesForUser(@PathVariable int accountId) {
        return ResponseEntity.ok()
            .body(messageService.findAllByUser(accountId));
    }
}
