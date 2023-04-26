package com.example.forexjsonrpc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RealtimeCurrencyExchangeRate {
    @JsonProperty("Realtime Currency Exchange Rate")
    private ExchangeRate exchangeRate;
}

