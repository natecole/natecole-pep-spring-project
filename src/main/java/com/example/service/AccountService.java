package com.example.service;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.RegistrationException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Attempts to create an account given an {@link Account} object using 
     * the {@link AccountRepository}.
     * 
     * @param account  The account to be created
     * @return The account if created successfully
     * @throws DuplicateUsernameException  Thrown when the username already exists
     *          in the database
     * @throws RegistrationException  Thrown when there is an error registering
     */
    public Optional<Account> createAccount(Account account) {

        if(account.getPassword().length() >= 4) {
            if (accountRepository.findByUsername(account.getUsername()) == null) {
                return Optional.of(accountRepository.save(account));
            } else {
                throw new DuplicateUsernameException(account.getUsername() + " already exists");
            }
        }
        else {
            throw new RegistrationException("Password must be at least 4 characters long.");
        }
    }

    /**
     * Searches for a matching pair of username and password using the
     * {@link AccountRepository}
     * 
     * @param account  The account to be logged in
     * @return The account if login was successful
     */
    public Optional<Account> login(Account account) {
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
    }
}
