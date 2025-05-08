package com.jug.demo.strategies;

import com.jug.demo.generated.models.TaxResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PortugalTaxCreditStrategy implements TaxCreditStrategy {

    private static final String COUNTRY = "Portugal";
    private static final String TAX_CREDIT = "0.15";

    @Override
    public boolean supports(String country) {
        return COUNTRY.equalsIgnoreCase(country);
    }

    public TaxResponse calculate(BigDecimal value) {
        return calculate(value, TAX_CREDIT);
    }

    @Override
    public TaxResponse calculate(BigDecimal value, String taxCredit) {
        return TaxCreditStrategy.super.calculate(value, taxCredit);
    }

}
