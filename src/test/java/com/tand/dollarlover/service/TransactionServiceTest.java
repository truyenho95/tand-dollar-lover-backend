package com.tand.dollarlover.service;

import com.tand.dollarlover.controller.TransactionController;
import com.tand.dollarlover.model.Transaction;
import com.tand.dollarlover.repository.TransactionRepository;
import com.tand.dollarlover.service.Impl.TransactionServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionService.class)
public class TransactionServiceTest {
    private static Transaction transaction;
    private static Page<Transaction> transactionList;
    private static List<Transaction> transactions;
    private static List<Transaction> emptyTransactions;

    @MockBean
    private TransactionRepository transactionRepository;

    @Captor
    private ArgumentCaptor<Transaction> transactionArgumentCaptor;

    @MockBean
    private TransactionController transactionController;

    @MockBean
    private TransactionServiceImpl transactionService;

    @AfterEach
    private void restMocks() {
        Mockito.reset(transactionRepository);
    }

    @Test
    public void testDummy() {
        Assertions.assertEquals(2, 1 + 1);
    }
}
