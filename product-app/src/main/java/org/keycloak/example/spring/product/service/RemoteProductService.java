package org.keycloak.example.spring.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.example.spring.product.web.domains.Product;
import org.keycloak.example.spring.product.web.domains.ProductList;
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

import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates making a call to ProductService
 */
@Service
public class RemoteProductService implements ProductService {
    public static final Logger log = LoggerFactory.getLogger(RemoteProductService.class);

    @Autowired
    private KeycloakRestTemplate template;

    @NotNull
    @Value("${database.service.url}")
    private String endpoint;

    ProductList products = new ProductList();
    ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public ProductList getProducts() {
        log.info("RemoteProductService : getproducts ");
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
            log.error(String.format("RemoteProductService :: Getting products Failed : ErrorCode :%s : Reason : %s", e.getMessage(), reason));
            e.printStackTrace();
            /*Default scenario*/
            products.add(new Product("default brand", "default name"));
            return products;
            /****/
        }

        try {
            products = mapper.readValue(response.getBody(), ProductList.class);
        } catch (IOException e) {
            log.error("IOException " + e.getMessage());
        }
        log.info("products :: {}", products.toString());

        return products;
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
