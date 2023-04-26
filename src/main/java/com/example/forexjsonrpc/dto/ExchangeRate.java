package com.example.forexjsonrpc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ExchangeRate {
    @JsonProperty("1. From_Currency Code")
    private String fromCurrencyCode;
    @JsonProperty("2. From_Currency Name")
    private String fromCurrencyName;
    @JsonProperty("3. To_Currency Code")
    private String toCurrencyCode;
    @JsonProperty("4. To_Currency Name")
    private String toCurrencyName;
    @JsonProperty("5. Exchange Rate")
    private String exchangeRate;
    @JsonProperty("6. Last Refreshed")
    private String lastRefreshed;
    @JsonProperty("7. Time Zone")
    private String timeZone;
    @JsonProperty("8. Bid Price")
    private String bidPrice;
    @JsonProperty("9. Ask Price")
    private String askPrice;
}


