package com.java.currency_converter.controller;

import com.java.currency_converter.model.CurrencySymbolsResponse;
import com.java.currency_converter.model.CurrencyConversionResponse;
import com.java.currency_converter.service.CurrencyServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class CurrencyControllerTest {

    @Mock
    private CurrencyServiceTest currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCurrency(){
        Map<String, String> symbols = new HashMap<>();
        symbols.put("USD", "United States Dollar");
        symbols.put("EUR", "Euro");
        CurrencySymbolsResponse response = new CurrencySymbolsResponse();
        response.setSymbols(symbols);

        // Define behavior for currencyService mock
        when(currencyService.getAllCurrency()).thenReturn(response);

        // Call the controller method and verify the response
        ResponseEntity<CurrencySymbolsResponse> result = currencyController.getAllCurrency();
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(symbols, result.getBody().getSymbols());
    }

    @Test
    void testCurrencyConvert() {
        // Prepare mock response
        CurrencyConversionResponse conversionResponse = new CurrencyConversionResponse();
        conversionResponse.setResult(74.5);

        // Define behavior for currencyService mock
        when(currencyService.currencyConvert("USD", "INR", 1)).thenReturn(conversionResponse);

        // Call the controller method and verify the response
        ResponseEntity<CurrencyConversionResponse> result = currencyController.currencyConvert("USD", "INR", 1);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(74.5, result.getBody().getResult());
    }

}
