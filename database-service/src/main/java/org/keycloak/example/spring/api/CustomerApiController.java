package org.keycloak.example.spring.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.rossillo.spring.web.mvc.CacheControl;
import net.rossillo.spring.web.mvc.CachePolicy;

import org.keycloak.adapters.tomcat.SimplePrincipal;
import org.keycloak.example.spring.api.domains.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * Customer API controller.
 */
@RestController
@RequestMapping("/customers")
@CacheControl(policy = CachePolicy.NO_CACHE)
public class CustomerApiController {

  private static final Logger log = LoggerFactory.getLogger(CustomerApiController.class);
  ObjectMapper mapper = new ObjectMapper();

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> getCustomers(Principal principal, HttpServletRequest request) {
    System.out.println("CustomerApiController : customer list");
    System.out.println("CustomerApiController : Principal :" + Optional.ofNullable(principal).orElse(new SimplePrincipal("ANONYMOUS")));
    System.out.println("CustomerApiController : Returning customer list.");
    List<Customer> customers = new ArrayList<>();
    Customer cs1 = new Customer("Mohamed", "ELHosni");
    Customer cs2 = new Customer("Mohamed", "Afdhal");
    customers.add(cs1);
    customers.add(cs2);
    String jsonInString = "";
    //Object to JSON in file
    try {
      mapper.writeValue(new File("classpath:customers.json"), customers);
      jsonInString = mapper.writeValueAsString(customers);
    } catch (IOException e) {
      System.err.println("IOException " + e.getMessage());
    }
    System.out.println("Mapper :: " + jsonInString);
    //Object to JSON in String
    return new ResponseEntity<String>(Optional.ofNullable(jsonInString).orElse("Empty Customer!"), HttpStatus.OK);
  }
}
