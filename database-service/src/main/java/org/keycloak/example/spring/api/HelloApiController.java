package org.keycloak.example.spring.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.rossillo.spring.web.mvc.CacheControl;
import net.rossillo.spring.web.mvc.CachePolicy;

import org.keycloak.adapters.tomcat.SimplePrincipal;
import org.keycloak.example.spring.api.domains.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Hello API controller.
 */
@RestController
@RequestMapping("/hello")
@CacheControl(policy = CachePolicy.NO_CACHE)
public class HelloApiController {

  private static final Logger log = LoggerFactory.getLogger(HelloApiController.class);
  private final AtomicLong counter = new AtomicLong();
  ObjectMapper mapper = new ObjectMapper();

  @RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> hello(Principal principal) {
    System.out.println("HelloApiController : HELLO");

    System.out.println("HelloApiController : Principal :" + Optional.ofNullable(principal).orElse(new SimplePrincipal("ANONYMOUS")));

    String hellomsg = " - Hello " + principal.getName() + " !";
    return new ResponseEntity<>(hellomsg, HttpStatus.OK);
  }

  @RequestMapping(path = "/greeting", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> greeting(Principal principal) {
    System.out.println("HelloApiController : GREETING");

    System.out.println("HelloApiController : Principal :" + Optional.ofNullable(principal).orElse(new SimplePrincipal("ANONYMOUS")));

    String hellomsg = "GREETING" ;
    List<Greeting> greetings= new ArrayList<>();
    Greeting greeting = new Greeting(counter.incrementAndGet(), hellomsg);
    Greeting greeting1 = new Greeting(counter.incrementAndGet(), hellomsg);
    Greeting greeting2 = new Greeting(counter.incrementAndGet(), hellomsg);
    greetings.add(greeting);
    greetings.add(greeting1);
    greetings.add(greeting2);
    String jsonInString = "";
    Greeting greeting0=new Greeting(0,"");
    try {
      mapper.writeValue(new File("classpath:greetings.json"), greetings);
      jsonInString = mapper.writeValueAsString(greetings);
    } catch (IOException e) {
      log.error("IOException "+e.getMessage());
    }
    log.info("jsonInString :: {}", jsonInString);
    //Object to JSON in String
    return    new ResponseEntity<String>(Optional.ofNullable(jsonInString).orElse("Empty Greeting!"), HttpStatus.OK);
  }
}
