package com.jug.demo.strategies;

import com.jug.demo.clients.TaxCreditClient;
import com.jug.demo.generated.models.TaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PortugalTaxCreditStrategy implements TaxCreditStrategy {

    private static final String COUNTRY = "Portugal";

    @Autowired
    private TaxCreditClient taxCreditClient;

    @Override
    public boolean supports(String country) {
        return COUNTRY.equalsIgnoreCase(country);
    }

    public TaxResponse calculate(BigDecimal value) {
        String taxCredit = taxCreditClient.getTaxCreditEur();
        return calculate(value, taxCredit);
    }

    @Override
    public TaxResponse calculate(BigDecimal value, String taxCredit) {
        return TaxCreditStrategy.super.calculate(value, taxCredit);
    }

}
