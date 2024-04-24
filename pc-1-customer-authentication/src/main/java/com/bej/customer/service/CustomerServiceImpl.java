package com.bej.customer.service;

import com.bej.customer.domain.Customer;
import com.bej.customer.exception.CustomerAlreadyExistException;
import com.bej.customer.exception.CustomerNotFoundException;
import com.bej.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService{
    private CustomerRepository customerRepository;
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }






    @Override
    public Customer saveCustomer(Customer customer) throws CustomerAlreadyExistException {

        //check if customer is present if condition is true then throw the exception CustomerAlreadyExistException
        if(customerRepository.findByCustomerNameAndCustomerPassword(customer.getCustomerName(), customer.getCustomerPassword()) != null){
            throw new CustomerAlreadyExistException( );
        }
        //else save the customer object
        Customer savedCustomer = customerRepository.save(customer);
        //and return the saved customer object


        return savedCustomer;
    }

    @Override
    public boolean deleteCustomer(int customerId) throws CustomerNotFoundException {

        //find the customerid if its empty then throw CustomerNotFoundException
        Customer customerToDelete = customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException());
        //else delete the customer
        customerRepository.delete(customerToDelete);
        //return true if successfully deleted


        return true;

    }

    @Override
    public Customer findByUsernameAndPassword(String username , String password) throws CustomerNotFoundException {
                // get the customer by calling findByCustomerNameAndCustomerPassword method
        // if customer is not null then return the customer object

        Customer customer = customerRepository.findByCustomerNameAndCustomerPassword(username, password);
        if(customer == null){
            throw new CustomerNotFoundException();
        }
        return customer;

    }

    @Override
    public List<Customer> getAllCustomer() throws Exception {

        // return all the customer from database

        return customerRepository.findAll();

    }


}
