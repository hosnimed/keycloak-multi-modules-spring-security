package org.keycloak.example.spring.customer.service.exceptions;

import org.keycloak.example.spring.customer.api.domains.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Optional;

import javax.security.sasl.AuthenticationException;

public class MyResponseErrorHandler implements ResponseErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyResponseErrorHandler.class);

    @Override
    public void handleError(ClientHttpResponse clienthttpresponse) throws IOException {

      if (clienthttpresponse.getStatusCode() == HttpStatus.FORBIDDEN) {
        logger.info(HttpStatus.FORBIDDEN + " response. Throwing authentication exception");
        throw new AuthenticationException();
      }
    }

    @Override
    public boolean hasError(ClientHttpResponse clienthttpresponse) throws IOException {

      if (clienthttpresponse.getStatusCode() != HttpStatus.OK) {
        logger.info("Status code: " + clienthttpresponse.getStatusCode());
        logger.info("Response" + clienthttpresponse.getStatusText());
        logger.info(clienthttpresponse.getStatusText());

        if (clienthttpresponse.getStatusCode() == HttpStatus.FORBIDDEN) {
          logger.info("Call returned a error 403 forbidden resposne ");
          return true;
        }

        if (clienthttpresponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
          logger.info("Call returned a error 500 Internal Server Exception ");

         /*
          InputStream is = clienthttpresponse.getBody();
          ObjectInputStream ois = new ObjectInputStream(is);
          Greeting obj =null;
          try {
             obj = (Greeting) ois.readObject();
          } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException thrown :  {}",e.getMessage());
          }
          catch (ClassCastException ex) {
            logger.error("ClassCastException thrown : {}",ex.getMessage());
          }
          finally {
            if (is != null) is.close();
          }
          logger.info("Mapping Object : {}", Optional.ofNullable(obj). orElse(new Greeting(0,"No Content!")));
         */
          return true;
        }
      }
      return false;
    }
}
