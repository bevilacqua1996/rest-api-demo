package com.jug.demo.services.impl;

import com.jug.demo.generated.models.TaxResponse;
import com.jug.demo.services.TaxCreditService;
import com.jug.demo.strategies.TaxCreditStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TaxCreditServiceImpl implements TaxCreditService {

    private final List<TaxCreditStrategy> strategies;

    public TaxCreditServiceImpl(List<TaxCreditStrategy> strategies) {
        this.strategies = strategies;
    }

    public TaxResponse calculateTaxCredit(String country, BigDecimal value) {
        for (TaxCreditStrategy strategy : strategies) {
            if (strategy.supports(country)) {
                return strategy.calculate(value);
            }
        }
        throw new IllegalArgumentException("No tax credit strategy found for country: " + country);
    }
}
