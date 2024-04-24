package com.mongoDB.PC1Customer.service;

import com.mongoDB.PC1Customer.domain.Customer;
import com.mongoDB.PC1Customer.exception.CustomerAlreadyExistsException;
import com.mongoDB.PC1Customer.exception.CustomerNotFoundException;
import com.mongoDB.PC1Customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private CustomerRepository customerRepository;
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }
    @Override
    public Customer saveCustomerDetail(Customer customer) throws CustomerAlreadyExistsException {
        if (customerRepository.findById(customer.getCustomerId()).isPresent())
        {
            throw new CustomerAlreadyExistsException();
        }
        return customerRepository.save(customer);
    }

    @Override
    public boolean deleteCustomer(int id) throws CustomerNotFoundException {
        boolean flag = false;
        if(customerRepository.findById(id).isEmpty())
        {
            throw new CustomerNotFoundException();
        }
        else {
            customerRepository.deleteById(id);
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Customer> getAllCustomerDetails() throws Exception {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> getAllCustomerWhoBoughtSamsungPhone(String productName) throws CustomerNotFoundException {
        if(customerRepository.findAllCustomerWhoBoughtSamsungPhone(productName).isEmpty())
        {
            throw new CustomerNotFoundException();
        }
        return customerRepository.findAllCustomerWhoBoughtSamsungPhone(productName);
    }
}
