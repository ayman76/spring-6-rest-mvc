package com.example.spring6restmvc.service.impl;

import com.example.spring6restmvc.exception.NotFoundException;
import com.example.spring6restmvc.model.Customer;
import com.example.spring6restmvc.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, Customer> customers;
    public CustomerServiceImpl() {
        this.customers = new HashMap<>();
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Ahmed")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Ayman")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Mohamed")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
        customers.put(customer3.getId(), customer3);
    }
    @Override
    public List<Customer> listCustomers() {
        log.debug("List Customers function was called in service");
        return new ArrayList<>(customers.values());
    }

    @Override
    public Optional<Customer> getCustomerById(UUID id) {
        log.debug("Get Customer By Id function was called in service");
        return Optional.of(customers.get(id));
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .customerName(customer.getCustomerName())
                .version(customer.getVersion())
                .build();

        customers.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        Customer existingCustomer = getCustomerById(customerId).orElseThrow(NotFoundException::new);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(customer.getVersion());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());

        customers.put(existingCustomer.getId(), existingCustomer);
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        customers.remove(customerId);
    }

    @Override
    public void updateCustomerPatchById(UUID customerId, Customer customer) {
        Customer existingCustomer = getCustomerById(customerId).orElseThrow(NotFoundException::new);

        if(StringUtils.hasText(customer.getCustomerName())){
            existingCustomer.setCustomerName(customer.getCustomerName());
        }
        if(customer.getVersion() != null){
            existingCustomer.setVersion(customer.getVersion());
        }
        existingCustomer.setLastModifiedDate(LocalDateTime.now());

        customers.put(existingCustomer.getId(), existingCustomer);
    }
}
