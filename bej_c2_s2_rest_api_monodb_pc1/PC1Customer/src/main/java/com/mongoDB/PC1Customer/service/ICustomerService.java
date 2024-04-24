package com.mongoDB.PC1Customer.service;

import com.mongoDB.PC1Customer.domain.Customer;
import com.mongoDB.PC1Customer.exception.CustomerAlreadyExistsException;
import com.mongoDB.PC1Customer.exception.CustomerNotFoundException;

import java.util.List;

public interface ICustomerService {
    Customer saveCustomerDetail(Customer customer) throws CustomerAlreadyExistsException;
    boolean deleteCustomer(int id) throws CustomerNotFoundException;
     List<Customer> getAllCustomerDetails() throws Exception;
     List<Customer> getAllCustomerWhoBoughtSamsungPhone(String productName)throws CustomerNotFoundException;

}
