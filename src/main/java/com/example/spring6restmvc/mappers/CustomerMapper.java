package com.example.spring6restmvc.mappers;

import com.example.spring6restmvc.entitiy.Customer;
import com.example.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDTO customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDTO dto);
}
