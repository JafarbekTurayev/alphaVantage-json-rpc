package com.example.forexjsonrpc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DailyTimeSeriesEntry {

    @JsonProperty("1. open")
    private String open;

    @JsonProperty("2. high")
    private String high;

    @JsonProperty("3. low")
    private String low;

    @JsonProperty("4. close")
    private String close;

    @JsonProperty("5. adjusted close")
    private String adjustedClose;

    @JsonProperty("6. volume")
    private String volume;

    @JsonProperty("7. dividend amount")
    private String dividendAmount;

    @JsonProperty("8. split coefficient")
    private String splitCoefficient;

}
