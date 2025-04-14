package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Register a new account if valid and username does not exist.
     */
    public Account register(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        if (username == null || username.isBlank() || password == null || password.length() < 4) {
            return null;
        }

        if (accountRepository.findByUsername(username).isPresent()) {
            return new Account(-1, username, password); // indicator for conflict
        }

        return accountRepository.save(account);
    }

    /**
     * Login a user if username/password match.
     */
    public Account login(String username, String password) {
        Optional<Account> found = accountRepository.findByUsername(username);
        if (found.isPresent() && found.get().getPassword().equals(password)) {
            return found.get();
        }
        return null;
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }
}
