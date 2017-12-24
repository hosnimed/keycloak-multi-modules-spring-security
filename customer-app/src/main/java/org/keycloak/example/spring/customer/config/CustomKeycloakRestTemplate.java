package org.keycloak.example.spring.customer.config;

import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.net.URI;

public class CustomKeycloakRestTemplate extends KeycloakRestTemplate {

  public static final Logger log = LoggerFactory.getLogger(CustomKeycloakRestTemplate.class);
  /**
   * Create a new instance based on the given {@link KeycloakClientRequestFactory}.
   *
   * @param factory the <code>KeycloakClientRequestFactory</code> to use when creating new requests
   */
  public CustomKeycloakRestTemplate(KeycloakClientRequestFactory factory) {
    super(factory);
  }

  @Override
  public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType)
      throws RestClientException {
    log.info("CustomKeycloakRestTemplate : URI : "+url.getRawPath());
    log.info("CustomKeycloakRestTemplate : HttpMethod : "+method.name());
    log.info("CustomKeycloakRestTemplate : HttpEntity : "+requestEntity.getBody());
    log.info("CustomKeycloakRestTemplate : ResponseType : "+responseType.getName());
    return super.exchange(url, method, requestEntity, responseType);
  }

}
