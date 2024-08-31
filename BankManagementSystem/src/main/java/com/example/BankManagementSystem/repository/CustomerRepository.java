package com.example.BankManagementSystem.repository;

import com.example.BankManagementSystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}