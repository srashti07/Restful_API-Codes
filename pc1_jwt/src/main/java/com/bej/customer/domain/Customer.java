package com.bej.customer.domain;




import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;



//Annotate the class with @Entity
@Entity
public class Customer {

    @Id
    private int customerId;
    private String customerName;
    private String customerPassword;
    private long customerPhoneNo;

    //create getter setters for all the attributes


    //override toString method

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerPassword='" + customerPassword + '\'' +
                ", customerPhoneNo=" + customerPhoneNo +
                '}';
    }

    public Customer(int customerId, String customerName, String customerPassword, long customerPhoneNo) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPassword = customerPassword;
        this.customerPhoneNo = customerPhoneNo;
    }

    public Customer() {

    }


    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }
    public void setCustomerName(String customerName)
    {
        this.customerName=customerName;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

}
