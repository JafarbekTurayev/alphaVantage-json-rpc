package com.example.forexjsonrpc;

import com.example.forexjsonrpc.domain.Transaction;
import com.example.forexjsonrpc.repository.TransactionRepository;
import com.example.forexjsonrpc.service.impl.TransactionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void testCreateTransaction() {
        String requestId = "1234";
        String functionName = "getStockData";
        String requestDetails = "{'param1': 'value1'}";
        String responseDetails = "{'result': 'success'}";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Transaction expectedTransaction = Transaction.builder()
                .requestId(requestId)
                .functionName(functionName)
                .requestDetails(requestDetails)
                .responseDetails(responseDetails)
                .createdAt(timestamp)
                .build();
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(expectedTransaction);

        transactionService.creatTransaction(requestId, functionName, requestDetails, responseDetails);

        verify(transactionRepository, times(1)).save(Mockito.any(Transaction.class));
        Mockito.verifyNoMoreInteractions(transactionRepository);
    }


    @Test
    public void testGetOne() {
        String requestId = "1234";
        String functionName = "getStockData";
        String requestDetails = "{'param1': 'value1'}";
        String responseDetails = "{'result': 'success'}";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Transaction transaction = Transaction.builder()
                .requestId(requestId)
                .functionName(functionName)
                .requestDetails(requestDetails)
                .responseDetails(responseDetails)
                .createdAt(timestamp)
                .build();;
        when(transactionRepository.findByRequestId(requestId)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getOne(requestId);

        assertEquals(transaction, result);
        assertEquals(requestId, result.getRequestId());
        assertEquals(functionName, result.getFunctionName());
        assertEquals(requestDetails, result.getRequestDetails());
        assertEquals(responseDetails, result.getResponseDetails());
        assertNotNull(result.getCreatedAt());
        assertEquals(timestamp, result.getCreatedAt());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOneNotFound() {
        String requestId = "1234";
        when(transactionRepository.findByRequestId(requestId)).thenReturn(Optional.empty());

        transactionService.getOne(requestId);
    }

    @Test
    public void testGetAll() {
        List<Transaction> expected = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findAll()).thenReturn(expected);

        List<Transaction> result = transactionService.getAll();

        assertEquals(expected, result);
        verify(transactionRepository, times(1)).findAll();
    }
}
