package com.example.forexjsonrpc.config;

import com.example.forexjsonrpc.dto.DailyTimeSeriesData;
import com.example.forexjsonrpc.dto.DailyTimeSeriesEntry;
import com.example.forexjsonrpc.dto.RealtimeCurrencyExchangeRate;
import com.example.forexjsonrpc.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlphaVantageClient {
    private final TransactionService transactionService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public static final String BASE_URL = "https://www.alphavantage.co/query";

    public DailyTimeSeriesData getDailyTimeSeriesData(String requestId, String symbol, String apiKey, String startDate, String endDate) throws IOException, ParseException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("function", "TIME_SERIES_DAILY_ADJUSTED")
                .queryParam("symbol", symbol)
                .queryParam("apikey", apiKey)
                .queryParam("outputSize","compact")
                .queryParam("timezone", "Asia/Tashkent");

        if (startDate!=null && endDate!=null) {
            builder.queryParam("startDate", startDate)
                    .queryParam("endDate", endDate);
        }

        String response = restTemplate.getForObject(builder.toUriString(), String.class);
        DailyTimeSeriesData data = objectMapper.readValue(response, DailyTimeSeriesData.class);
        Map<String, DailyTimeSeriesEntry> timeSeries = data.getTimeSeries();
        Map<String, DailyTimeSeriesEntry> filteredMap = new HashMap<>();

        if (startDate!=null && endDate!=null){
            for (String key : timeSeries.keySet()) {
                DailyTimeSeriesEntry dailyTimeSeriesEntry = timeSeries.get(key);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                if (format.parse(key).after(format.parse(startDate)) && format.parse(key).before(format.parse(endDate))){
                    filteredMap.put(key,dailyTimeSeriesEntry);
                }
            }
            data.getTimeSeries().clear();
            data.setTimeSeries(filteredMap);
        }
        transactionService.creatTransaction(requestId, "TIME_SERIES_DAILY_ADJUSTED", builder.toUriString(), String.valueOf(data));
        return data;
    }

    public RealtimeCurrencyExchangeRate getRealtimeCurrencyExchangeRate(String requestId, String fromCurrency, String toCurrency, String apiKey) throws IOException {
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("function", "CURRENCY_EXCHANGE_RATE")
                .queryParam("from_currency", fromCurrency)
                .queryParam("to_currency", toCurrency)
                .queryParam("apikey", apiKey)
                .build()
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);
        if (response == null || response.isEmpty()) {
            throw new RuntimeException("Empty response from API");
        }
        RealtimeCurrencyExchangeRate exchangeRate = objectMapper.readValue(response, RealtimeCurrencyExchangeRate.class);
        if (exchangeRate == null) {
            throw new RuntimeException("Error response from API: " + response);
        }
        transactionService.creatTransaction(requestId, "CURRENCY_EXCHANGE_RATE", url, response);
        return exchangeRate;
    }
}
