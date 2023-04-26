package com.example.forexjsonrpc.service;

import com.example.forexjsonrpc.domain.Transaction;

import java.util.List;



public interface TransactionService {
    void creatTransaction(String requestId, String functionName, String requestDetails, String responseDetails);

    Transaction getOne(String requestId);

    List<Transaction> getAll();
}
