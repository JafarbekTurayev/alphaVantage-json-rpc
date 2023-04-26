package com.example.forexjsonrpc.client;

import com.example.forexjsonrpc.domain.Transaction;
import com.example.forexjsonrpc.dto.DailyTimeSeriesData;
import com.example.forexjsonrpc.dto.RealtimeCurrencyExchangeRate;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class RpcClient {
    public static void main(String[] args) throws Throwable {
//        getTransaction("291fcd3f-fe18-4626-b6dd-2b28f3fbeb43");
//        getCurrencyExchangeRate();
//        getStockData();
    }

    public static void getStockData() throws Throwable {

        JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://localhost:8080/api"));

        HashMap<String, String> params = new HashMap<>();
        params.put("requestId", UUID.randomUUID().toString());
        params.put("symbol", "IBM");
        params.put("apiKey", "3BAK0PED9883LHPX");
        params.put("startDate", "2023-04-18"); //or null
        params.put("endDate", "2023-04-25"); //or null
        DailyTimeSeriesData response = client.invoke("getStockData", params, DailyTimeSeriesData.class);
    }

    public static void getCurrencyExchangeRate() throws Throwable {

        JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://localhost:8080/api"));

        HashMap<String, String> params = new HashMap<>();
        params.put("requestId", UUID.randomUUID().toString());
        params.put("fromCurrency", "USD");
        params.put("toCurrency", "UZS");
        params.put("apiKey", "3BAK0PED9883LHPX");

        RealtimeCurrencyExchangeRate response = client.invoke("getCurrencyExchangeRate", params, RealtimeCurrencyExchangeRate.class);
    }

    public static void getTransaction(String id) throws Throwable {

        JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://localhost:8080/api"));

        HashMap<String, String> params = new HashMap<>();
        params.put("requestId", id);
        Transaction response = client.invoke("getTransaction", params, Transaction.class);
    }


}
