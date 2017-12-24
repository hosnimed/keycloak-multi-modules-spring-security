package org.keycloak.example.spring.customer.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.example.spring.customer.api.domains.Customer;
import org.keycloak.example.spring.customer.api.domains.CustomerList;
import org.keycloak.example.spring.customer.api.domains.Greeting;
import org.keycloak.example.spring.customer.api.domains.GreetingList;
import org.keycloak.example.spring.customer.service.exceptions.MyResponseErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

import javax.validation.constraints.NotNull;

/**
 * Demonstrates making a call to CustomerService
 */
@Service
public class RemoteCustomerService implements CustomerService {

  public static final Logger log = LoggerFactory.getLogger(RemoteCustomerService.class);

  @Autowired
  private KeycloakRestTemplate template;

  @NotNull
  @Value("${database.service.url}")
  private String endpoint;

  @NotNull
  @Value("${database.service.greetingUrl}")
  private String greetingEndpoint;

  GreetingList greetings = new GreetingList();
  CustomerList customers = new CustomerList();
  ObjectMapper mapper = new ObjectMapper();


  @Override
  public GreetingList hello() {
    log.info("RemoteCustomerService : hello ");
    KeycloakSecurityContext session = getKeycloakSecurityContext();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "text/plain");
    headers.set("Authorization", "Bearer " + session.getTokenString());
    HttpEntity<?> entity = new HttpEntity<String>(headers);
    log.info(String.format("Hello  Headers :: %s", entity.getHeaders().entrySet().toString()));

    ResponseEntity<String> response = null;
    try {
      template.setErrorHandler(new MyResponseErrorHandler());
      response = template.exchange(greetingEndpoint, HttpMethod.GET, entity, String.class);
      log.info("RESPONSE " + response.getBody());
    } catch (RestClientException e) {
      /****/
      e.printStackTrace();
      /*Default scenario*/
      greetings.add(new Greeting(0, "Hello Call fail :/"));
      return greetings;
      /****/
    }

    try {
      greetings = mapper.readValue(response.getBody(), GreetingList.class);
    } catch (IOException e) {
      log.error("IOException " + e.getMessage());
    }
    log.info("greetings :: {}", greetings.toString());

    return greetings;
  }


  @Override
  public CustomerList getCustomers() {
    log.info("RemoteCustomerService : getCustomers ");
    KeycloakSecurityContext session = getKeycloakSecurityContext();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "text/plain");
    headers.set("Authorization", "Bearer " + session.getTokenString());
    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
    log.info(String.format("KeycloakRestTemplate  getHeaders :: %s", entity.getHeaders().entrySet().toString()));

    ResponseEntity<String> response = null;
    String reason = null;
    try {
      response = template.exchange(endpoint, HttpMethod.GET, entity, String.class);
    } catch (RestClientException e) {
      reason = (response == null) ? "Null ResponseValue" : "Response with Default Value";
      log.error(String.format("RemoteCustomerService :: Getting Customers Failed : ErrorCode :%s : Reason : %s", e.getMessage(), reason));
      e.printStackTrace();
      /*Default scenario*/
      customers.add(new Customer("default firstname","default lastname"));
      return customers;
      /****/
    }

    try {
      customers = mapper.readValue(response.getBody(), CustomerList.class);
    } catch (IOException e) {
      log.error("IOException " + e.getMessage());
    }
    log.info("customers :: {}", customers.toString());

    return customers;
  }


  /**
   * Returns the {@link KeycloakSecurityContext} from the Spring {@link SecurityContextHolder}'s {@link Authentication}.
   *
   * @return the current <code>KeycloakSecurityContext</code>
   */
  protected KeycloakSecurityContext getKeycloakSecurityContext() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    KeycloakAuthenticationToken token;
    KeycloakSecurityContext context;

    if (authentication == null) {
      throw new IllegalStateException("Cannot set authorization header because there is no authenticated principal");
    }

    if (!KeycloakAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
      throw new IllegalStateException(
          String.format(
              "Cannot set authorization header because Authentication is of type %s but %s is required",
              authentication.getClass(), KeycloakAuthenticationToken.class)
      );
    }

    token = (KeycloakAuthenticationToken) authentication;
    context = token.getAccount().getKeycloakSecurityContext();

    return context;
  }


}