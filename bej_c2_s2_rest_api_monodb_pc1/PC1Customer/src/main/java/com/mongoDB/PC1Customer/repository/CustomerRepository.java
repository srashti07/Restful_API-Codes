package com.mongoDB.PC1Customer.repository;

import com.mongoDB.PC1Customer.domain.Customer;
import com.mongoDB.PC1Customer.exception.CustomerAlreadyExistsException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer,Integer> {
    @Query("{'customerProduct.productName':{$in : [?0]}}")
    List<Customer> findAllCustomerWhoBoughtSamsungPhone(String productName);


}
