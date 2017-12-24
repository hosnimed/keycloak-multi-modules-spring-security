package org.keycloak.example.spring.customer.service;

import org.keycloak.example.spring.customer.api.domains.CustomerList;
import org.keycloak.example.spring.customer.api.domains.GreetingList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


public interface CustomerService {

    /**
     *
     * Say Hello Principal
     */
    GreetingList hello();

    /**
     * Returns a list of customers.
     */
    CustomerList getCustomers();

}
