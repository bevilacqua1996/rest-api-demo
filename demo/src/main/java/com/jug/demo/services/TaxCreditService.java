package com.jug.demo.services;

import com.jug.demo.generated.models.TaxResponse;

import java.math.BigDecimal;

public interface TaxCreditService {

    public TaxResponse calculateTaxCredit(String country, BigDecimal value);
}
