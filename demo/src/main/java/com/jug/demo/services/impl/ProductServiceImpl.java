package com.jug.demo.services.impl;

import com.jug.demo.entities.ProductEntity;
import com.jug.demo.exceptions.ClientNotFoundException;
import com.jug.demo.generated.models.ProductRequest;
import com.jug.demo.generated.models.ProductResponse;
import com.jug.demo.mappers.ProductMapper;
import com.jug.demo.repositories.ClientRepository;
import com.jug.demo.repositories.ProductRepository;
import com.jug.demo.services.ProductService;
import com.jug.demo.templates.GenerateProductReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productRequest.getName());
        productEntity.setPrice(productRequest.getPrice());
        productEntity.setClient(clientRepository.findById(Long.valueOf(productRequest.getClient())).
                orElseThrow(ClientNotFoundException::new));
        ProductEntity savedProduct = productRepository.save(productEntity);
        return ProductMapper.mapToResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponse> getProductById(Integer id) {
        return productRepository.findById(Long.valueOf(id))
                .map(ProductMapper::mapToResponse);
    }

    @Override
    public ProductResponse updateProduct(Integer id, ProductRequest productRequest) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(Long.valueOf(id));
        if (optionalProduct.isPresent()) {
            ProductEntity productEntity = optionalProduct.get();
            productEntity.setName(productRequest.getName());
            productEntity.setPrice(productRequest.getPrice());
            productEntity.setClient(clientRepository.findById(Long.valueOf(productRequest.getClient())).
                    orElseThrow(ClientNotFoundException::new));
            ProductEntity updatedProduct = productRepository.save(productEntity);
            return ProductMapper.mapToResponse(updatedProduct);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }

    @Override
    public void deleteProduct(Integer id) {
        if (productRepository.existsById(Long.valueOf(id))) {
            productRepository.deleteById(Long.valueOf(id));
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    @Override
    public String getProductReport(Integer id) {
        return new GenerateProductReport(id, productRepository).generate();
    }


}