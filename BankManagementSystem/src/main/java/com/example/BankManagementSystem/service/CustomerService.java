package com.example.BankManagementSystem.service;

import com.example.BankManagementSystem.model.Customer;
import com.example.BankManagementSystem.repository.AccountRepository;
import com.example.BankManagementSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if (accountRepository.findAll().stream().anyMatch(account -> account.getCustomer().getId().equals(id))) {
            throw new RuntimeException("Cannot delete customer connected to an account");
        }
        customerRepository.deleteById(id);
    }

    public List<Customer> findByName(String name) {
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
}