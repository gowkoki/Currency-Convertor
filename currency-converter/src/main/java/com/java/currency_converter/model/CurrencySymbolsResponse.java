package com.java.currency_converter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class CurrencySymbolsResponse {
    @JsonProperty("symbols")
    private Map<String, String> symbols;
}
