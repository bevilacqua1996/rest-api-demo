package com.jug.demo.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "taxCreditClient", url = "${tax-credit.url}")
public interface TaxCreditClient {

    @GetMapping("/brl")
    String getTaxCreditBrl();

    @GetMapping("/eur")
    String getTaxCreditEur();

    @GetMapping("/mzn")
    String getTaxCreditMzn();

}