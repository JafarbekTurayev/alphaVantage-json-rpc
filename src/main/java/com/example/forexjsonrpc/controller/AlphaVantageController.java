package com.example.forexjsonrpc.controller;

import com.example.forexjsonrpc.config.AlphaVantageClient;
import com.example.forexjsonrpc.domain.Transaction;
import com.example.forexjsonrpc.dto.DailyTimeSeriesData;
import com.example.forexjsonrpc.dto.RealtimeCurrencyExchangeRate;
import com.example.forexjsonrpc.service.TransactionService;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.ParseException;

@Controller
@JsonRpcService("/api")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
public class AlphaVantageController {

    private final AlphaVantageClient alphaVantageClient;
    private final TransactionService transactionService;
    @JsonRpcMethod("getStockData")
    public DailyTimeSeriesData getStockData(@JsonRpcParam("requestId") String requestId,
                                            @JsonRpcParam("symbol") String symbol,
                                            @JsonRpcParam("apiKey") String apiKey,
                                            @Nullable @JsonRpcParam("startDate") String startDate,
                                            @Nullable @JsonRpcParam("endDate") String endDate) throws IOException, ParseException {
        return alphaVantageClient.getDailyTimeSeriesData(requestId, symbol, apiKey, startDate, endDate);
    }

    @JsonRpcMethod("getCurrencyExchangeRate")
    public RealtimeCurrencyExchangeRate getCurrencyExchangeRate(@JsonRpcParam("requestId") String requestId, @JsonRpcParam("fromCurrency") String fromCurrency, @JsonRpcParam("toCurrency") String toCurrency, @JsonRpcParam("apiKey") String apiKey) throws IOException {
        return alphaVantageClient.getRealtimeCurrencyExchangeRate(requestId, fromCurrency, toCurrency, apiKey);
    }

    @JsonRpcMethod("getTransaction")
    public Transaction getTransaction(@JsonRpcParam("requestId") String requestId) {
        return transactionService.getOne(requestId);
    }
}
