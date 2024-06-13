package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.MessageNotFoundException;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Creates a message using {@link MessageRepository}
     * 
     * @param message  The message to be created
     * @return The message if successful
     */
    public Message createMessage(Message message){
        return messageRepository.save(message);
    }

    /**
     * Gets all messages using {@link MessageRepository}
     * 
     * @return A list of all messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Gets a message by its messageId using {@link MessageRepository}
     * 
     * @param id
     * @return The message if it exists
     */
    public Optional<Message> getMessage(int id) {
        return Optional.ofNullable(messageRepository.getById(id));
    }

    /**
     * Deletes a message by its messageId using {@link MessageRepository}
     * 
     * @param id
     * @return The number of rows affected if successful
     * @throws MessageNotFoundException is thrown when the messageId 
     *          does not exist in the database
     */
    public int deleteMessage(int id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isEmpty()) {
            throw new MessageNotFoundException(String.format("The messageId %d cannot be found", id));
        } else {
            messageRepository.delete(message.get());
            return 1;
        }
    }

    /**
     * Updates a message with new messageText by its messageId 
     * using {@link MessageRepository}
     * 
     * @param id
     * @param newText
     * @return The number of rows affected if successful
     * @throws MessageNotFoundException is thrown when the messageId 
     *          does not exist in the database
     */
    public int updateMessage(int id, String newText) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isEmpty()) {
            throw new MessageNotFoundException(String.format("The messageId %d cannot be found", id));
        } else {
            message.get().setMessageText(newText);
            messageRepository.save(message.get());
            return 1;
        }
    }

    /**
     * Retrieves all messages for a account specified by its id using
     * {@link MessageRepository}
     * 
     * @param id
     * @return A list of all messages posted by the user
     */
    public List<Message> findAllByUser(int id) {
        return messageRepository.findAllByPostedBy(id);
    }
}
