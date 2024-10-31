package com.java.currency_converter.service;

import com.java.currency_converter.model.CurrencyConversionResponse;
import com.java.currency_converter.model.CurrencySymbolsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpHeaders;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {

    @Value("${rapidapi.key}")
    private String apiKey;

    @Value("${rapidapi.host}")
    private String apiHost;

    @Value("${currency.api.symbols.url}")
    private String symbolUrl;

    @Value("${currency.api.convert.url}")
    private String convertUrl;

    private final RestTemplate restTemplate;

    public CurrencySymbolsResponse getAllCurrency() {
        try {
            // Set the headers for API request
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-rapidapi-key", apiKey);
            headers.set("x-rapidapi-host", apiHost);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // send get request to API
            ResponseEntity<CurrencySymbolsResponse> response = restTemplate.exchange(symbolUrl, HttpMethod.GET, entity, CurrencySymbolsResponse.class);

            log.info("Symbols API:{}", response.getBody());

            return response.getBody();

        } catch (Exception e) {
            log.error("Error fetching currency symbols", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAiled to fetch currency symbols.");
        }

    }


    public CurrencyConversionResponse currencyConvert(String fromCurrency, String toCurrency, double amount) {
        try {
            // Set the headers for API request
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-rapidapi-key", apiKey);
            headers.set("x-rapidapi-host", apiHost);

            // Construct the API URL dynamically
            String currencyConvertUrl = String.format("%s?from=%s&to=%s&amount=%s",convertUrl, fromCurrency, toCurrency, amount);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // send get request to API
            ResponseEntity<CurrencyConversionResponse> response = restTemplate.exchange(currencyConvertUrl, HttpMethod.GET, entity, CurrencyConversionResponse.class);

            log.info("Conversion API Response:{}", response.getBody());

            return response.getBody();

        } catch (Exception e) {
            log.error("Error converting currency", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to convert currency.");
        }
    }
}
