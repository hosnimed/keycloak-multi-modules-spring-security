package org.keycloak.example.spring.api.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable{

  @JsonProperty("brand")
  private String brand;
  @JsonProperty("name")
  private String name;

  public Product() {
  }

  public Product(String brand, String name) {
    this.brand = brand;
    this.name = name;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "{" +
           "brand=" + brand +
           ", name='" + name + '\'' +
           '}';
  }
}