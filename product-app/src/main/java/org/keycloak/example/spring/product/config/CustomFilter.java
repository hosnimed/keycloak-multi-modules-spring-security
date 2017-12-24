package org.keycloak.example.spring.product.config;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.adapters.tomcat.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if(authentication != null && authentication.isAuthenticated() ){
        log.info("Principal : {} Authorities : {}",authentication.getPrincipal(), authentication.getAuthorities());
          KeycloakSecurityContext keycloakSecurityContext = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
          if (keycloakSecurityContext != null ){
            log.info("Token : {} Permissions : {}", keycloakSecurityContext.getToken(), keycloakSecurityContext.getAuthorizationContext().getPermissions().toString());
          }
        /*
        * Mock Simle User Role
        * */
        /*
        if(authentication.getAuthorities().isEmpty()){
          Set<String> roles = new HashSet<>();
          roles.add("user");
          KeycloakAccount account = new SimpleKeycloakAccount(new SimplePrincipal("Mocked_User"), roles, new RefreshableKeycloakSecurityContext());

          KeycloakAuthenticationProvider provider = new KeycloakAuthenticationProvider();
          SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
          grantedAuthorityMapper.setPrefix("ROLE_");
          grantedAuthorityMapper.setConvertToUpperCase(true);
          provider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);

          KeycloakAuthenticationToken token = new KeycloakAuthenticationToken(account, false);

          Authentication auth = provider.authenticate(token);

          SecurityContextHolder.clearContext();
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
        */
      }
    }
    if (response != null) {
      log.info("CustomFilter : ServletResponse : Status : {} : HeaderNames : {}" , response.getStatus(), response.getHeaderNames().toString());
    }


    filterChain.doFilter(servletRequest, servletResponse);
  }
}
