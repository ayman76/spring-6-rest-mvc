package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.model.CustomerDTO;
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
class CustomerDTOControllerTest {

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
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor = ArgumentCaptor.forClass(CustomerDTO.class);


    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testUpdateCustomerPatch() throws Exception{
        CustomerDTO testCustomerDTO = customerServiceImpl.listCustomers().get(0);
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New Name");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, testCustomerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerPatchById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(testCustomerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }

    @Test
    void testUpdateCustomer() throws Exception{
        CustomerDTO testCustomerDTO = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, testCustomerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomerDTO)));

        verify(customerService).updateCustomerById(any(UUID.class), any(CustomerDTO.class));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDTO testCustomerDTO = customerServiceImpl.listCustomers().get(0);

        given(customerService.deleteCustomerById(any(UUID.class))).willReturn(true);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, testCustomerDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(testCustomerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testCreateCustomer() throws Exception{
        CustomerDTO testCustomerDTO = customerServiceImpl.listCustomers().get(0);
        testCustomerDTO.setId(null);
        testCustomerDTO.setVersion(null);

        given(customerService.createCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(testCustomerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testListCustomers() throws Exception{
        List<CustomerDTO> customerDTOS = customerServiceImpl.listCustomers();

        given(customerService.listCustomers()).willReturn(customerDTOS);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(customerDTOS.size())));
    }

    @Test
    void testGetCustomerById() throws Exception {
        CustomerDTO testCustomerDTO = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(testCustomerDTO.getId())).willReturn(Optional.of(testCustomerDTO));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomerDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testCustomerDTO.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(testCustomerDTO.getCustomerName())));
    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception{

        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}