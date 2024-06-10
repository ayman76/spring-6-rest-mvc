package com.example.spring6restmvc.service.impl;

import com.example.spring6restmvc.exception.NotFoundException;
import com.example.spring6restmvc.model.CustomerDTO;
import com.example.spring6restmvc.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, CustomerDTO> customers;
    public CustomerServiceImpl() {
    }
    @Override
    public List<CustomerDTO> listCustomers() {
        log.debug("List Customers function was called in service");
        return new ArrayList<>(customers.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        log.debug("Get Customer By Id function was called in service");
        return Optional.of(customers.get(id));
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        CustomerDTO savedCustomerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .customerName(customerDTO.getCustomerName())
                .version(customerDTO.getVersion())
                .build();

        customers.put(savedCustomerDTO.getId(), savedCustomerDTO);
        return savedCustomerDTO;
    }

    @Override
    public void updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        CustomerDTO existingCustomerDTO = getCustomerById(customerId).orElseThrow(NotFoundException::new);
        existingCustomerDTO.setCustomerName(customerDTO.getCustomerName());
        existingCustomerDTO.setVersion(customerDTO.getVersion());
        existingCustomerDTO.setLastModifiedDate(LocalDateTime.now());

        customers.put(existingCustomerDTO.getId(), existingCustomerDTO);
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        customers.remove(customerId);
    }

    @Override
    public void updateCustomerPatchById(UUID customerId, CustomerDTO customerDTO) {
        CustomerDTO existingCustomerDTO = getCustomerById(customerId).orElseThrow(NotFoundException::new);

        if(StringUtils.hasText(customerDTO.getCustomerName())){
            existingCustomerDTO.setCustomerName(customerDTO.getCustomerName());
        }
        if(customerDTO.getVersion() != null){
            existingCustomerDTO.setVersion(customerDTO.getVersion());
        }
        existingCustomerDTO.setLastModifiedDate(LocalDateTime.now());

        customers.put(existingCustomerDTO.getId(), existingCustomerDTO);
    }
}
