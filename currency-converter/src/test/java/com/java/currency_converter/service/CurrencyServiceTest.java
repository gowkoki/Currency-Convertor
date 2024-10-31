package com.java.currency_converter.service;

import com.java.currency_converter.model.CurrencyConversionResponse;
import com.java.currency_converter.model.CurrencySymbolsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyService currencyService;

    @Value("${currency.api.symbols.url}")
    private String symbolUrl = "https://api.example.com/symbols";

    @Value("${currency.api.convert.url}")
    private String convertUrl = "https://api.example.com/convert";

    @Value("${rapidapi.key}")
    private String apiKey = "test_api_key";

    @Value("${rapidapi.host}")
    private String apiHost = "test_api_host";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCurrency() {
        // Prepare mock headers and response
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        CurrencySymbolsResponse response = new CurrencySymbolsResponse();
        Map<String, String> symbols = new HashMap<>();
        symbols.put("USD", "United States Dollar");
        symbols.put("EUR", "Euro");
        response.setSymbols(symbols);

        // Mock the RestTemplate response
        when(restTemplate.exchange(symbolUrl, HttpMethod.GET, entity, CurrencySymbolsResponse.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        // Call the service method and verify the result
        CurrencySymbolsResponse result = currencyService.getAllCurrency();
        assertNotNull(result);
        assertEquals(symbols, result.getSymbols());
    }

    @Test
    void testCurrencyConvert() {
        // Prepare mock headers and response
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        CurrencyConversionResponse conversionResponse = new CurrencyConversionResponse();
        conversionResponse.setResult(74.5);

        String currencyConvertUrl = String.format("%s?from=%s&to=%s&amount=%s", convertUrl, "USD", "INR", 1);

        // Mock the RestTemplate response
        when(restTemplate.exchange(currencyConvertUrl, HttpMethod.GET, entity, CurrencyConversionResponse.class))
                .thenReturn(new ResponseEntity<>(conversionResponse, HttpStatus.OK));

        // Call the service method and verify the result
        CurrencyConversionResponse result = currencyService.currencyConvert("USD", "INR", 1);
        assertNotNull(result);
        assertEquals(74.5, result.getResult());
    }

    @Test
    void testCurrencyConvertThrowsException() {
        // Prepare mock headers and URL
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String currencyConvertUrl = String.format("%s?from=%s&to=%s&amount=%s", convertUrl, "USD", "INR", 1);

        // Mock the RestTemplate to throw an exception
        when(restTemplate.exchange(currencyConvertUrl, HttpMethod.GET, entity, CurrencyConversionResponse.class))
                .thenThrow(new RuntimeException("API Error"));

        // Verify that the service method throws a ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> currencyService.currencyConvert("USD", "INR", 1));
    }

}
