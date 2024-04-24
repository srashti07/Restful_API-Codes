package com.mongoDB.PC1Customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongoDB.PC1Customer.controller.CustomerController;
import com.mongoDB.PC1Customer.domain.Customer;
import com.mongoDB.PC1Customer.domain.Product;
import com.mongoDB.PC1Customer.exception.CustomerAlreadyExistsException;
import com.mongoDB.PC1Customer.exception.CustomerNotFoundException;
import com.mongoDB.PC1Customer.service.CustomerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;
    @Mock
    private CustomerServiceImpl customerService;

    @InjectMocks
    private CustomerController customerController;
    private Customer customer1, customer2;
    private Product product1, product2;

    List<Customer> customerList;
    @BeforeEach
    void setUp(){
        product1 = new Product(101,"Samsung", "Mobile Phone");
        customer1 = new Customer(201,"Annanya",914045,product1);
        product2 = new Product(102,"Noise","Headphone");
        customer2 = new Customer(202,"Tricha",98765,product2);
        customerList = Arrays.asList(customer1,customer2);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();


    }
    @AfterEach
    void tearDown(){
        customer1 = null;
        customer2 = null;
    }
    @Test
    public void givenCustomerToSaveReturnSavedCustomer() throws Exception{
        when (customerService.saveCustomerDetail(any())).thenReturn(customer1);
        mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(customer1)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(customerService,times(1)).saveCustomerDetail(any());
    }
    @Test
    public void givenCustomerToSaveReturnSavedCustomerFailure() throws Exception {
        when(customerService.saveCustomerDetail(any())).thenThrow(CustomerAlreadyExistsException.class);
        mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(customer1)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(customerService,times(1)).saveCustomerDetail(any());
    }
    @Test
    public void givenCustomerIdDeleteCustomer() throws Exception {
        when(customerService.deleteCustomer(anyInt())).thenReturn(true);
        mockMvc.perform(delete("/api/v1/customer/201")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(customer1)))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(customerService,times(1)).deleteCustomer(anyInt());
    }
    @Test
    public void givenCustomerIdDeleteCustomerFailure() throws Exception {
        when(customerService.deleteCustomer(anyInt())).thenThrow(CustomerNotFoundException.class);
        mockMvc.perform(delete("/api/v1/customer/201")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
        verify(customerService,times(1)).deleteCustomer(anyInt());

    }
    @Test
    public void getAllCustomerShouldReturnCustomerList() throws Exception {
        when(customerService.getAllCustomerDetails()).thenReturn(customerList);
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(customerList.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value(customer1.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerId").value(customer2.getCustomerId()))
                .andDo(MockMvcResultHandlers.print());
        verify(customerService, times(1)).getAllCustomerDetails();
    }

    @Test
    public void getAllCustomerShouldReturnInternalServerErrorOnServiceException() throws Exception {
        when(customerService.getAllCustomerDetails()).thenThrow(RuntimeException.class);
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());
        verify(customerService, times(1)).getAllCustomerDetails();
    }

    @Test
    public void getAllCustomerWhoBoughtSamsungPhoneShouldReturnCustomerList() throws Exception {
        String productName = "Samsung";
        when(customerService.getAllCustomerWhoBoughtSamsungPhone(productName)).thenReturn(customerList);

        mockMvc.perform(get("/api/v1/customers/{productName}", productName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(customerList.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value(customer1.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerId").value(customer2.getCustomerId()))
                .andDo(MockMvcResultHandlers.print());

        verify(customerService, times(1)).getAllCustomerWhoBoughtSamsungPhone(productName);
    }

    @Test
    public void getAllCustomerWhoBoughtSamsungPhoneShouldReturnInternalServerErrorOnServiceException() throws Exception {
        String productName = "Samsung";
        when(customerService.getAllCustomerWhoBoughtSamsungPhone(productName)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/v1/customers/{productName}", productName))
                .andExpect(status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());

        verify(customerService, times(1)).getAllCustomerWhoBoughtSamsungPhone(productName);
    }





    private static String jsonToString(final Object ob) throws JsonProcessingException {
        String result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(ob);
            result = jsonContent;
        } catch(JsonProcessingException e) {
            result = "JSON processing error";
        }

        return result;
    }



}
