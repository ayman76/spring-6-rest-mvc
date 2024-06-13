package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.entitiy.Customer;
import com.example.spring6restmvc.exception.NotFoundException;
import com.example.spring6restmvc.mappers.CustomerMapper;
import com.example.spring6restmvc.model.CustomerDTO;
import com.example.spring6restmvc.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testPatchCustomerTooLongName() throws Exception{
        Customer customer = customerRepository.findAll().get(0);
        Map<String, Object> customerMap= new HashMap<>();

        customerMap.put("customerName", "Ahmed 12321312231231232131223123123213122312312321312231231232131223123123213122312312321312231231232131223123");

        MvcResult mvcResult = mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1))).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testListCustomers() {
        List<CustomerDTO> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testListCustomersEmpty() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testGetCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testSaveCustomer() {
        CustomerDTO dto = CustomerDTO.builder().customerName("Ahmed").build();

        ResponseEntity responseEntity = customerController.createCustomer(dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] locationPath = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID customerId = UUID.fromString(locationPath[4]);

        Customer customer = customerRepository.findById(customerId).get();
        assertThat(customer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerMapper.customerToCustomerDto(customer);
        dto.setId(null);
        dto.setVersion(null);

        String customerName = "Alaa";
        dto.setCustomerName(customerName);

        ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
    }

    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });

    }

    @Transactional
    @Rollback
    @Test
    void testDeleteCustomer() {
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity responseEntity = customerController.deleteCustoemrById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(customerRepository.findById(customer.getId())).isEmpty();

    }

    @Test
    void testDeleteCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustoemrById(UUID.randomUUID());
        });
    }
}