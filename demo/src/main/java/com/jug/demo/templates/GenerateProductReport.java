package com.jug.demo.templates;

import com.jug.demo.generated.models.ProductResponse;
import com.jug.demo.mappers.ProductMapper;
import com.jug.demo.repositories.ProductRepository;

import java.util.Optional;

public class GenerateProductReport extends ReportGenerator {

    private final ProductRepository productRepository;
    private final Integer id;
    private String dataProcessed;

    public GenerateProductReport(Integer id, ProductRepository productRepository) {
        super(id, productRepository);
        this.id = id;
        this.productRepository = productRepository;
    }


    @Override
    protected Optional<ProductResponse> loadData() {
        return Optional.of(productRepository.findById(Long.valueOf(id))
                .map(ProductMapper::mapToResponse).orElseThrow(RuntimeException::new));
    }

    @Override
    protected void process(Object data) {
        ProductResponse productResponse = ((Optional<ProductResponse>) data).get();
        dataProcessed = productResponse.getName() + ", Price: " + productResponse.getPrice();
    }

    @Override
    protected String export() {
        return"Product Report: "+ this.dataProcessed;
    }
}
