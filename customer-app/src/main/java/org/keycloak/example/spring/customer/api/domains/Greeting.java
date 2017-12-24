package org.keycloak.example.spring.customer.api.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Greeting implements Serializable {

  public static final Long serialVerionId = 1L;

  private  long id;
  private  String content;

  public Greeting() {
  }

  public Greeting(long id, String content) {
    this.id = id;
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "{" +
           "id=" + id +
           ", content='" + content + '\'' +
           '}';
  }
}