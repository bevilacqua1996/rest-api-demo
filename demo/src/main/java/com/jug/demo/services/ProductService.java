package com.jug.demo.services;

import com.jug.demo.generated.models.ProductRequest;
import com.jug.demo.generated.models.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductResponse createProduct(ProductRequest ProductRequest);

    List<ProductResponse> getAllProducts();

    Optional<ProductResponse> getProductById(Integer id);

    ProductResponse updateProduct(Integer id, ProductRequest ProductRequest);

    void deleteProduct(Integer id);

    String getProductReport(Integer id);
    
}
