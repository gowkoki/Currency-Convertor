package com.java.currency_converter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyConversionResponse {
    @JsonProperty("result")
    private double result;
}
