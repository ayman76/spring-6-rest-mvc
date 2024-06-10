package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entitiy.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
