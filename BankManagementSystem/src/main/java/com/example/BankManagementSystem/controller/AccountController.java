package com.example.BankManagementSystem.controller;

import com.example.BankManagementSystem.model.Account;
import com.example.BankManagementSystem.service.AccountService;
import com.example.BankManagementSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account saveAccount(@RequestBody Account account) {
        if (account.getCustomer() == null || customerService.findById(account.getCustomer().getId()).isEmpty()) {
            throw new IllegalArgumentException("Customer must exist to add an account");
        }
        return accountService.saveAccount(account);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

    @GetMapping("/search")
    public List<Account> searchAccounts(@RequestParam String accountNumber) {
        return accountService.findByAccountNumber(accountNumber);
    }
}