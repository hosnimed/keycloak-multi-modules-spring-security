package org.keycloak.example.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomFilter extends GenericFilterBean {

  public static final Logger log = LoggerFactory.getLogger(CustomFilter.class);

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    log.info("CustomFilter");
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    if (request != null) {
      log.info("CustomFilter : ServletRequest : HeaderNames : {} : {}", request.getHeader("Authorization"), request.getHeader("Accept"));
    }
    if (response != null) {
      log.info("CustomFilter : ServletResponse : Status : {} : HeaderNames : {}" , response.getStatus(), response.getHeaderNames().toString());
    }


    filterChain.doFilter(servletRequest, servletResponse);
  }
}
