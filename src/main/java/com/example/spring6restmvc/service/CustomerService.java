package com.example.spring6restmvc.service;

import com.example.spring6restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<Customer> listCustomers();
    Optional<Customer> getCustomerById(UUID id);

    Customer createCustomer(Customer customer);

    void updateCustomerById(UUID customerId, Customer customer);

    void deleteCustomerById(UUID customerId);

    void updateCustomerPatchById(UUID customerId, Customer customer);
}
