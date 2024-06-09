package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.exception.NotFoundException;
import com.example.spring6restmvc.model.Customer;
import com.example.spring6restmvc.service.CustomerService;
import com.example.spring6restmvc.service.impl.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);


    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testUpdateCustomerPatch() throws Exception{
        Customer testCustomer = customerServiceImpl.listCustomers().get(0);
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New Name");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerPatchById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(testCustomer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }

    @Test
    void testUpdateCustomer() throws Exception{
        Customer testCustomer = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)));

        verify(customerService).updateCustomerById(any(UUID.class), any(Customer.class));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Customer testCustomer = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(testCustomer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testCreateCustomer() throws Exception{
        Customer testCustomer = customerServiceImpl.listCustomers().get(0);
        testCustomer.setId(null);
        testCustomer.setVersion(null);

        given(customerService.createCustomer(any(Customer.class))).willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(testCustomer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testListCustomers() throws Exception{
        List<Customer> customers = customerServiceImpl.listCustomers();

        given(customerService.listCustomers()).willReturn(customers);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(customers.size())));
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer testCustomer = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName())));
    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception{

        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}