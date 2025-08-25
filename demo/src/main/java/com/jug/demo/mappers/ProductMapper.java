package com.jug.demo.mappers;

import com.jug.demo.entities.ProductEntity;
import com.jug.demo.generated.models.ProductResponse;

import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductResponse mapToResponse(ProductEntity productEntity) {
        ProductResponse response = new ProductResponse();
        response.setId(Math.toIntExact(productEntity.getId()));
        response.setName(productEntity.getName());
        response.setPrice(productEntity.getPrice());
        response.setClients(productEntity.getClients().stream()
                .map(client -> Math.toIntExact(client.getId()))
                .collect(Collectors.toList()));
        return response;
    }
}
