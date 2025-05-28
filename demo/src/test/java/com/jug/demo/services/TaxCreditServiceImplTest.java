package com.jug.demo.services;

import com.jug.demo.generated.models.TaxResponse;
import com.jug.demo.services.impl.TaxCreditServiceImpl;
import com.jug.demo.strategies.TaxCreditStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxCreditServiceImplTest {

    @Mock
    private TaxCreditStrategy brazilStrategy;

    @Mock
    private TaxCreditStrategy portugalStrategy;

    @Mock
    private TaxCreditStrategy mocambiqueStrategy;

    private TaxCreditServiceImpl taxCreditService;

    @Test
    void testCalculateTaxCredit_Brazil() {
        // Arrange
        taxCreditService = new TaxCreditServiceImpl(List.of(brazilStrategy, portugalStrategy, mocambiqueStrategy));

        String country = "Brazil";
        BigDecimal value = new BigDecimal("100.00");

        TaxResponse expectedResponse = new TaxResponse();
        expectedResponse.setTaxAmount(new BigDecimal("10.00"));
        expectedResponse.setFinalPrice(new BigDecimal("110.00"));
        expectedResponse.setTaxRate(new BigDecimal("0.10"));

        when(brazilStrategy.supports(country)).thenReturn(true);
        when(brazilStrategy.calculate(value)).thenReturn(expectedResponse);

        // Act
        TaxResponse response = taxCreditService.calculateTaxCredit(country, value);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("10.00"), response.getTaxAmount());
        assertEquals(new BigDecimal("110.00"), response.getFinalPrice());
        assertEquals(new BigDecimal("0.10"), response.getTaxRate());

        verify(brazilStrategy).supports(country);
        verify(brazilStrategy).calculate(value);
        // Since brazilStrategy.supports returns true, the other strategies should not be checked
        verify(portugalStrategy, never()).supports(country);
        verify(mocambiqueStrategy, never()).supports(country);
        verify(portugalStrategy, never()).calculate(any(BigDecimal.class));
        verify(mocambiqueStrategy, never()).calculate(any(BigDecimal.class));
    }

    @Test
    void testCalculateTaxCredit_Portugal() {
        // Arrange
        taxCreditService = new TaxCreditServiceImpl(List.of(brazilStrategy, portugalStrategy, mocambiqueStrategy));

        String country = "Portugal";
        BigDecimal value = new BigDecimal("100.00");

        TaxResponse expectedResponse = new TaxResponse();
        expectedResponse.setTaxAmount(new BigDecimal("23.00"));
        expectedResponse.setFinalPrice(new BigDecimal("123.00"));
        expectedResponse.setTaxRate(new BigDecimal("0.23"));

        when(brazilStrategy.supports(country)).thenReturn(false);
        when(portugalStrategy.supports(country)).thenReturn(true);
        when(portugalStrategy.calculate(value)).thenReturn(expectedResponse);

        // Act
        TaxResponse response = taxCreditService.calculateTaxCredit(country, value);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("23.00"), response.getTaxAmount());
        assertEquals(new BigDecimal("123.00"), response.getFinalPrice());
        assertEquals(new BigDecimal("0.23"), response.getTaxRate());

        verify(brazilStrategy).supports(country);
        verify(portugalStrategy).supports(country);
        verify(portugalStrategy).calculate(value);
        // Since portugalStrategy.supports returns true, the mocambiqueStrategy should not be checked
        verify(mocambiqueStrategy, never()).supports(country);
        verify(brazilStrategy, never()).calculate(any(BigDecimal.class));
        verify(mocambiqueStrategy, never()).calculate(any(BigDecimal.class));
    }

    @Test
    void testCalculateTaxCredit_UnsupportedCountry() {
        // Arrange
        taxCreditService = new TaxCreditServiceImpl(List.of(brazilStrategy, portugalStrategy, mocambiqueStrategy));

        String country = "UnsupportedCountry";
        BigDecimal value = new BigDecimal("100.00");

        when(brazilStrategy.supports(country)).thenReturn(false);
        when(portugalStrategy.supports(country)).thenReturn(false);
        when(mocambiqueStrategy.supports(country)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> taxCreditService.calculateTaxCredit(country, value));

        assertEquals("No tax credit strategy found for country: UnsupportedCountry", exception.getMessage());

        verify(brazilStrategy).supports(country);
        verify(portugalStrategy).supports(country);
        verify(mocambiqueStrategy).supports(country);
        verify(brazilStrategy, never()).calculate(any(BigDecimal.class));
        verify(portugalStrategy, never()).calculate(any(BigDecimal.class));
        verify(mocambiqueStrategy, never()).calculate(any(BigDecimal.class));
    }
}
