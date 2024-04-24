package com.mongoDB.PC1Customer;

import com.mongoDB.PC1Customer.domain.Customer;
import com.mongoDB.PC1Customer.domain.Product;
import com.mongoDB.PC1Customer.exception.CustomerAlreadyExistsException;
import com.mongoDB.PC1Customer.exception.CustomerNotFoundException;
import com.mongoDB.PC1Customer.repository.CustomerRepository;
import com.mongodb.DuplicateKeyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.annotation.Id;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    private Product product;
    private Customer customer;
    @BeforeEach
    void setUp(){
        product = new Product(1201,"Samsung","Mobile Phone");
        customer  = new Customer(1101, "Annanya", 1234567893, product);
    }
    @AfterEach
    void tearDown(){
        product = null;
        customer = null;
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Test case for saving customer object")
    void givenCustomerToSaveShouldReturnSavedCustomer(){
        customerRepository.save(customer);
        Customer customer1= customerRepository.findById(customer.getCustomerId()).get();
        assertNotNull(customer1);
        assertEquals(customer.getCustomerId(),customer1.getCustomerId());
    }
    @Test
    @DisplayName("Test case for saving customer object - Failure: Incorrect customer ID")
    void givenIncorrectCustomerIdShouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> {
            // Attempt to retrieve a customer with an incorrect ID
            Customer incorrectCustomer = customerRepository.findById(customer.getCustomerId()).get();
        });
    }


    @Test
    @DisplayName("Test case for deleting customer object")
    public void givenCustomerToDeleteShouldSeleteCustomer(){
        customerRepository.insert(customer);
        Customer customer1 = customerRepository.findById(customer.getCustomerId()).get();
        customerRepository.delete(customer1);
        assertEquals(Optional.empty(),customerRepository.findById(customer.getCustomerId()));
    }

    @Test
    @DisplayName("Test case for deleting customer object - Failure: Customer not found")
    void givenNonExistingCustomerToDeleteShouldThrowException() {
        assertThrows(CustomerNotFoundException.class, () -> {
            // Attempt to delete a non-existing customer
            Optional<Customer> nonExistingCustomer = customerRepository.findById(501);
            if (nonExistingCustomer.isEmpty()) {
                throw new CustomerNotFoundException();
            }
            customerRepository.delete(nonExistingCustomer.get());
        });
    }

    @Test
    @DisplayName("Test case for retrieving all the customer object")
    public void givenCustomerReturnAllCustomerDetails(){

        customerRepository.insert(customer);
        Product product1 = new Product(701,"Oppo","mobile Phone");
        Customer customer1 = new Customer(801,"Tricha",7865433,product1);
        customerRepository.insert(customer1);

        List<Customer> list = customerRepository.findAll();
        assertEquals(2, list.size());
        assertEquals("Tricha",list.get(1).getCustomerName());

    }
    @Test
    @DisplayName("Test case for retrieving all customer objects - Failure: No customers found")
    void givenNoCustomersReturnEmptyList() {
        // Ensure the repository is empty
        customerRepository.deleteAll();

        List<Customer> list = customerRepository.findAll();
        assertTrue(list.isEmpty());
    }
    @Test
    @DisplayName("Test case for retrieving all customer objects - Failure: Incorrect number of customers")
    void givenIncorrectNumberOfCustomersShouldThrowException() {
        // Insert only one customer
        customerRepository.insert(customer);

        assertThrows(AssertionFailedError.class, () -> {
            // Attempt to retrieve all customers, expecting 2
            List<Customer> list = customerRepository.findAll();
            assertEquals(2, list.size());
        });
    }
    @Test
    @DisplayName("Test case for retrieving all customer objects - Failure: Incorrect customer details")
    void givenIncorrectCustomerDetailsShouldThrowException() {
        customerRepository.insert(customer);

        assertThrows(AssertionFailedError.class, () -> {
            // Modify customer details after insertion
            customer.setCustomerName("UpdatedName");

            // Attempt to retrieve all customers and check for incorrect details
            List<Customer> list = customerRepository.findAll();
            assertEquals("Tricha", list.get(0).getCustomerName());
        });
    }

    @Test
    @DisplayName("Test case for retrieving all customers who bought a Samsung phone")
    void givenProductShouldReturnAllCustomerWhoBoughtSamsungPhone(){
        Product samsungProduct = new Product(1201,"Samsung","Mobile Phone");
        Customer customer = new Customer(1101,"Annanya",1234567893,samsungProduct);

        customerRepository.save(customer);

        List<Customer> samsungCustomers = customerRepository. findAllCustomerWhoBoughtSamsungPhone("Samsung");

        assertNotNull(samsungCustomers);
        assertEquals(1, samsungCustomers.size());
        assertEquals(customer.getCustomerId(), samsungCustomers.get(0).getCustomerId());
        assertEquals(customer.getCustomerName(), samsungCustomers.get(0).getCustomerName());
//       assertEquals(customer.getPhoneNumber(), samsungCustomers.get(0).getPhoneNumber());
        assertEquals(customer.getCustomerProduct().getProductName(), samsungCustomers.get(0).getCustomerProduct().getProductName());

    }

    @Test
    @DisplayName("Test case for retrieving all customers who bought a Samsung phone - Failure: No customers found")
    void givenNonExistingProductShouldReturnEmptyList() {
        // Ensure the repository is empty
        customerRepository.deleteAll();

        List<Customer> samsungCustomers = customerRepository.findAllCustomerWhoBoughtSamsungPhone("Samsung");

        assertNotNull(samsungCustomers);
        assertTrue(samsungCustomers.isEmpty());
    }




}
