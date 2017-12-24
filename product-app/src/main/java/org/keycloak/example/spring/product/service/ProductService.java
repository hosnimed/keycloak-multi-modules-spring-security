package org.keycloak.example.spring.product.service;

import org.keycloak.example.spring.product.web.domains.ProductList;

import java.util.List;

public interface ProductService {

    /**
     * Returns a list of products.
     */
    ProductList getProducts();
}
