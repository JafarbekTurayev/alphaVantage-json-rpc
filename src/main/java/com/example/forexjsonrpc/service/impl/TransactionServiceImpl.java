package com.example.forexjsonrpc.service.impl;

import com.example.forexjsonrpc.domain.Transaction;
import com.example.forexjsonrpc.repository.TransactionRepository;
import com.example.forexjsonrpc.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    @Override
    public void creatTransaction(String requestId, String functionName, String requestDetails, String responseDetails) {
        log.info("Create Transaction Started");
        Transaction transaction = transactionRepository.save(Transaction.builder()
                .requestId(requestId)
                .functionName(functionName)
                .requestDetails(requestDetails)
                .responseDetails(responseDetails)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build());
        log.info("Saved Transaction" + transaction);
    }

    @Override
    public Transaction getOne(String requestId) {
        return transactionRepository.findByRequestId(requestId).orElseThrow();
    }

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }
}
