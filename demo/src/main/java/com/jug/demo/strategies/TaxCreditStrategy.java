package com.jug.demo.strategies;

import com.jug.demo.generated.models.TaxResponse;

import java.math.BigDecimal;

public interface TaxCreditStrategy {

    boolean supports(String country);

    TaxResponse calculate(BigDecimal value);

    default TaxResponse calculate(BigDecimal value, String taxCredit) {
        BigDecimal taxAmount = value.multiply(new BigDecimal(taxCredit));
        TaxResponse taxResponse = new TaxResponse();
        taxResponse.setTaxAmount(taxAmount);
        taxResponse.setFinalPrice(value.add(taxAmount));
        taxResponse.setTaxRate(new BigDecimal(taxCredit));
        return taxResponse;
    };
}
