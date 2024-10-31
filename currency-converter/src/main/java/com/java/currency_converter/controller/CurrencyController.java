package com.java.currency_converter.controller;

import com.java.currency_converter.model.CurrencyConversionResponse;
import com.java.currency_converter.model.CurrencySymbolsResponse;
import com.java.currency_converter.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//import javax.validation.constraints.Min;

@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
@Validated
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/allCurrency")
    public ResponseEntity<CurrencySymbolsResponse> getAllCurrency() {
        CurrencySymbolsResponse symbols = currencyService.getAllCurrency();
        return ResponseEntity.ok(symbols);
    }

    @GetMapping("/convert")
    public ResponseEntity<CurrencyConversionResponse> currencyConvert(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount) {

        CurrencyConversionResponse conversion = currencyService.currencyConvert(from, to , amount);
        return ResponseEntity.ok(conversion);
    }

}



