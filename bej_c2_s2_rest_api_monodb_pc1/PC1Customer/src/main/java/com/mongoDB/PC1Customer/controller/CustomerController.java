package com.mongoDB.PC1Customer.controller;

import com.mongoDB.PC1Customer.domain.Customer;
import com.mongoDB.PC1Customer.exception.CustomerAlreadyExistsException;
import com.mongoDB.PC1Customer.exception.CustomerNotFoundException;
import com.mongoDB.PC1Customer.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
public class CustomerController {

    private ResponseEntity responseEntity;
    private ICustomerService iCustomerService;
    @Autowired
    public CustomerController(final ICustomerService iCustomerService){
        this.iCustomerService = iCustomerService;
    }
    @PostMapping("customer")
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) throws CustomerAlreadyExistsException{
        try{
            iCustomerService.saveCustomerDetail(customer);
            responseEntity = new ResponseEntity(customer, HttpStatus.CREATED);
        } catch (CustomerAlreadyExistsException e){
            throw new CustomerAlreadyExistsException();
        }
        catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @DeleteMapping("customer/{customerId}")
    public ResponseEntity<?>deleteCustomer(@PathVariable("customerId")int customerId) throws CustomerNotFoundException{
        try{
            iCustomerService.deleteCustomer(customerId);
            responseEntity = new ResponseEntity<>("Successfully deleted !!!",HttpStatus.OK);
        } catch (CustomerNotFoundException e){
            throw new CustomerNotFoundException();
        }
        catch (Exception exception){
            responseEntity = new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @GetMapping("customers")
    public ResponseEntity<?> getAllCustomer(){
        try{
            responseEntity = new ResponseEntity(iCustomerService.getAllCustomerDetails(),HttpStatus.OK);
        }catch (Exception exception){
            responseEntity = new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @GetMapping("customers/{productName}")
    public ResponseEntity<?> getAllCustomerWhoBoughtSamsungPhone(@PathVariable String productName){
        try{
            responseEntity = new ResponseEntity(iCustomerService.getAllCustomerWhoBoughtSamsungPhone(productName),HttpStatus.OK);
        }catch (Exception exception){
            responseEntity = new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }







}
