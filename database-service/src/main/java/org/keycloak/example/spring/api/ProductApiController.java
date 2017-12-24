package org.keycloak.example.spring.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.rossillo.spring.web.mvc.CacheControl;
import net.rossillo.spring.web.mvc.CachePolicy;

import org.keycloak.example.spring.api.domains.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Product API controller.
 */
@RestController
@RequestMapping("/products")
@CacheControl(policy = CachePolicy.NO_CACHE)
public class ProductApiController {

    private static final Logger log = LoggerFactory.getLogger(ProductApiController.class);
    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getProducts(Principal principal) {
        log.info("ProductApiController : product list");
        log.info("ProductApiController : Principal :" + Optional.ofNullable(principal.getName()).orElse("ANONYMOUS"));

        List<Product> products = new ArrayList<>();
        Product p1 = new Product("Apple", "Macbook Pro");
        Product p2 = new Product("Microsoft", "Surface Pro");
        products.add(p1);
        products.add(p2);
        String jsonInString = "";
        //Object to JSON in file
        try {
            mapper.writeValue(new File("classpath:products.json"), products);
            jsonInString = mapper.writeValueAsString(products);
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        }
        System.out.println("Mapper :: " + jsonInString);
        //Object to JSON in String
        return new ResponseEntity<String>(Optional.ofNullable(jsonInString).orElse("Empty Product!"), HttpStatus.OK);
}

}


