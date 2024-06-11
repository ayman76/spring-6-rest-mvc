package com.example.spring6restmvc.service;

import com.example.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO);

    Boolean deleteCustomerById(UUID customerId);

    void updateCustomerPatchById(UUID customerId, CustomerDTO customerDTO);
}
