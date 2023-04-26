package com.example.forexjsonrpc;

import com.example.forexjsonrpc.config.AlphaVantageClient;
import com.example.forexjsonrpc.dto.DailyTimeSeriesData;
import com.example.forexjsonrpc.dto.RealtimeCurrencyExchangeRate;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AlphaVantageClientIntegrationTest {

    @Autowired
    private AlphaVantageClient alphaVantageClient;

    @Test
    void testGetDailyTimeSeriesData() throws Exception {
        // Given
        String requestId = "1";
        String symbol = "IBM";
        String apiKey = "TEST";
        String startDate = "2023-04-01";
        String endDate = "2023-04-26";

        // When
        DailyTimeSeriesData actualResponse = alphaVantageClient.getDailyTimeSeriesData(requestId, symbol, apiKey, startDate, endDate);

        // Then
        assertNotNull(actualResponse);
        assertNotNull(actualResponse.getMetaData());
        assertNotNull(actualResponse.getTimeSeries());
        assertFalse(actualResponse.getTimeSeries().isEmpty());
        assertTrue(actualResponse.getMetaData().containsValue(symbol));
    }

    @Test
    void testGetRealtimeCurrencyExchangeRate() throws Exception {
        // Given
        String requestId = "1";
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        String apiKey = "TEST";

        // When
        RealtimeCurrencyExchangeRate result = alphaVantageClient.getRealtimeCurrencyExchangeRate(requestId, fromCurrency, toCurrency, apiKey);

        // Then
        assertNotNull(result);
        assertNotNull(result.getExchangeRate());
        assertNotNull(result.getExchangeRate().getFromCurrencyCode());
        assertNotNull(result.getExchangeRate().getToCurrencyCode());
        assertTrue(result.getExchangeRate().getFromCurrencyCode().contains(fromCurrency));
        assertTrue(result.getExchangeRate().getToCurrencyCode().contains(toCurrency));
    }
}

