package com.jug.demo.controllers;

import com.jug.demo.generated.controllers.ProductApi;
import com.jug.demo.generated.models.ProductRequest;
import com.jug.demo.generated.models.ProductResponse;
import com.jug.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController implements ProductApi {

    @Autowired
    private ProductService productService;

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductRequest productRequest) {
        ProductResponse productResponse = productService.createProduct(productRequest);
        return ResponseEntity.ok(productResponse);
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProductList() {
        List<ProductResponse> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Integer id) {
        Optional<ProductResponse> productResponse = productService.getProductById(id);
        return productResponse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(Integer id, ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<String> getProductReport(Integer id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.getProductReport(id));
    }
}
