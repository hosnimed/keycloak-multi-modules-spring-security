package org.keycloak.example.spring.api.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Greeting implements Serializable{

  @JsonProperty("id")
  private long id;
  @JsonProperty("content")
  private String content;

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