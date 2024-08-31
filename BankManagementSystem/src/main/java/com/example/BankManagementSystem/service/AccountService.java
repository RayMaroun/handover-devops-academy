package com.example.BankManagementSystem.service;

import com.example.BankManagementSystem.model.Account;
import com.example.BankManagementSystem.repository.AccountRepository;
import com.example.BankManagementSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account saveAccount(Account account) {
        if (account.getCustomer() == null || customerRepository.findById(account.getCustomer().getId()).isEmpty()) {
            throw new IllegalArgumentException("Customer must exist to add an account");
        }
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public List<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findAll().stream()
                .filter(account -> account.getAccountNumber().toLowerCase().contains(accountNumber.toLowerCase()))
                .toList();

    }
}