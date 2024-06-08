package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.model.Customer;
import com.example.spring6restmvc.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PatchMapping("{customerId}")
    public ResponseEntity updateCustomerPatchById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerPatchById(customerId, customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + customerId);

        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity deleteCustoemrById(@PathVariable("customerId") UUID customerId) {
        customerService.deleteCustomerById(customerId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PutMapping("{customerId}")
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + customerId);

        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity createCustomer(@RequestBody Customer customer){
        Customer savedCustomer = customerService.createCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers() {
        log.debug("List Customers Functions called in controller");
        return customerService.listCustomers();
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId")UUID customerId){
        log.debug("Get Customer By Id Functions called in controller");
        return customerService.getCustomerById(customerId);
    }
}
