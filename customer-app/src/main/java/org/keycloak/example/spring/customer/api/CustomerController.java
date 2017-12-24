package org.keycloak.example.spring.customer.api;

import net.rossillo.spring.web.mvc.CacheControl;
import net.rossillo.spring.web.mvc.CachePolicy;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.example.spring.customer.api.domains.CustomerList;
import org.keycloak.example.spring.customer.api.domains.GreetingList;
import org.keycloak.example.spring.customer.service.CustomerService;
import org.keycloak.example.spring.customer.service.RemoteCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

/**
 * Customer portal controller.
 */
@Controller
@CacheControl(policy = CachePolicy.NO_CACHE)
public class CustomerController {

  private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

  @Autowired
  private CustomerService customerService;

  @NotNull
  @Value("${product.service.url}")
  private String productServiceUrl;

  @NotNull
  @Value("${customer.service.url}")
  private String customerServiceUrl;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String handleHomePageRequest(Model model) {
    return "home";
  }

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public String handleHelloPageRequest( Model model) {
    GreetingList greetings = new GreetingList();
    greetings = customerService.hello();
    model.addAttribute("greetings", greetings);
    return "hello";
  }

  @RequestMapping(value = "/customers", method = RequestMethod.GET)
  public String handleCustomersRequest(Principal principal, Model model) {
    CustomerList customers = new CustomerList();
    customers = customerService.getCustomers();
    log.info("Getting customers  : " + customers);
    model.addAttribute("customers", customers);
    model.addAttribute("principal", principal);
    return "customers";
  }

  @RequestMapping(value = "/admin", method = RequestMethod.GET)
  public String handleAdminRequest(Principal principal, Model model) {
    model.addAttribute("principal", principal);
    return "admin";
  }

  @ModelAttribute("productServiceUrl")
  public String populateProductServiceUrl() {
    return productServiceUrl;
  }

  @ModelAttribute("customerServiceUrl")
  public String populateCustomerServiceUrl() {
    return customerServiceUrl;
  }

  @ModelAttribute("serviceName")
  public String populateServiceName() {
    return "Customer Portal";
  }
}
