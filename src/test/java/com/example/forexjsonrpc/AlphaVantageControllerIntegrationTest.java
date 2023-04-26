package com.example.forexjsonrpc;

import com.example.forexjsonrpc.config.AlphaVantageClient;
import com.example.forexjsonrpc.domain.Transaction;
import com.example.forexjsonrpc.dto.DailyTimeSeriesData;
import com.example.forexjsonrpc.dto.RealtimeCurrencyExchangeRate;
import com.example.forexjsonrpc.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AlphaVantageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlphaVantageClient alphaVantageClient;

    @MockBean
    private TransactionService transactionService;

    @Test
    void testGetStockData() throws Exception {
        String requestId = "12345";
        String symbol = "IBM";
        String apiKey = "TEST";
        String startDate = "2023-04-01";
        String endDate = "2023-04-26";
        DailyTimeSeriesData expectedResponse = new DailyTimeSeriesData();
        when(alphaVantageClient.getDailyTimeSeriesData(requestId, symbol, apiKey, startDate, endDate)).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jsonrpc\": \"2.0\", \"method\": \"getStockData\", \"params\": { \"requestId\": \"" + requestId + "\", \"symbol\": \"" + symbol + "\", \"apiKey\": \"" + apiKey + "\", \"startDate\": \"" + startDate + "\", \"endDate\": \"" + endDate + "\" }, \"id\": 1 }")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(alphaVantageClient).getDailyTimeSeriesData(requestId, symbol, apiKey, startDate, endDate);

        String expectedJson = "{\"jsonrpc\":\"2.0\",\"result\":{},\"id\":1}";
        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    void testGetCurrencyExchangeRate() throws Exception {
        String requestId = "12345";
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        String apiKey = "TEST";
        RealtimeCurrencyExchangeRate expectedResponse = new RealtimeCurrencyExchangeRate();
        when(alphaVantageClient.getRealtimeCurrencyExchangeRate(requestId, fromCurrency, toCurrency, apiKey)).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jsonrpc\": \"2.0\", \"method\": \"getCurrencyExchangeRate\", \"params\": { \"requestId\": \"" + requestId + "\", \"fromCurrency\": \"" + fromCurrency + "\", \"toCurrency\": \"" + toCurrency + "\", \"apiKey\": \"" + apiKey + "\" }, \"id\": 1 }")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(alphaVantageClient).getRealtimeCurrencyExchangeRate(requestId, fromCurrency, toCurrency, apiKey);

        String expectedJson = "{\"jsonrpc\":\"2.0\",\"result\":{},\"id\":1}";
        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
    }

        @Test
    void testGetTransaction() throws Exception {
        // given
        String requestId = "12345";
        Transaction expectedTransaction = new Transaction(1L,requestId,
                "CURRENCY_EXCHANGE_RATE",
                "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=USD&to_currency=UZS&apikey=3BAK0PED9883LHPX",
                "{\n" +
                        "    \"Realtime Currency Exchange Rate\": {\n" +
                        "        \"1. From_Currency Code\": \"USD\",\n" +
                        "        \"2. From_Currency Name\": \"United States Dollar\",\n" +
                        "        \"3. To_Currency Code\": \"UZS\",\n" +
                        "        \"4. To_Currency Name\": \"Uzbekistan Som\",\n" +
                        "        \"5. Exchange Rate\": \"11395.00000000\",\n" +
                        "        \"6. Last Refreshed\": \"2023-04-26 00:00:00\",\n" +
                        "        \"7. Time Zone\": \"UTC\",\n" +
                        "        \"8. Bid Price\": \"11394.70000000\",\n" +
                        "        \"9. Ask Price\": \"11395.30000000\"\n" +
                        "    }\n" +
                        "}",
                Timestamp.valueOf("2023-04-26 00:00:00"));
        when(transactionService.getOne(requestId)).thenReturn(expectedTransaction);

        // when
        MvcResult result = mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jsonrpc\":\"2.0\",\"method\":\"getTransaction\",\"params\":{\"requestId\":\"12345\"},\"id\":1}"))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String responseJson = result.getResponse().getContentAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseNode = objectMapper.readTree(responseJson);
        JsonNode resultNode = responseNode.get("result");

        assertNotNull(resultNode);
        assertEquals(expectedTransaction.getId(), resultNode.get("id").asLong());
        assertEquals(expectedTransaction.getFunctionName(), resultNode.get("functionName").asText());
        assertEquals(expectedTransaction.getRequestDetails(), resultNode.get("requestDetails").asText());
        assertEquals(expectedTransaction.getResponseDetails(), resultNode.get("responseDetails").asText());
    }
}
