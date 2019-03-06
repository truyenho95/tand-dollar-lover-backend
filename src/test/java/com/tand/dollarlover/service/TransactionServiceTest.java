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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionService.class)
public class TransactionServiceTest {
    private static Transaction transaction;
    private static Page<Transaction> transactionList;
    private static List<Transaction> transactions;
    private static List<Transaction> emptyTransactions;

    static {
        transaction = new Transaction(10000, true, "something");
        transaction.setId(1L);
        transactions = Arrays.asList(transaction);
        transactionList = new PageImpl<>(transactions);
    }

    @MockBean
    private TransactionRepository transactionRepository;
    @Captor
    private ArgumentCaptor<Transaction> transactionArgumentCaptor;
    @MockBean
    private TransactionController transactionController;

    @Autowired
    private TransactionService transactionService;

    @AfterEach
    private void restMocks() {
        Mockito.reset(transactionRepository);
    }

    @Test
    public void testDummy() {
        Assertions.assertEquals(2, 1 + 1);
    }

    @Test
    public void testSaveANewTransaction() {
        transactionService.save(transaction);
        Assertions.assertEquals(transactionArgumentCaptor.getValue().getAmount(), 10000);
        verify(transactionRepository, times(1)).save(transactionArgumentCaptor.capture());
    }

    @Test
    public void testDeleteATransactionById() {
        transactionService.remove(transaction.getId());
        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void findAllTransactionsWhichHaveOneTransaction() {
        when(transactionRepository.findAll()).thenReturn(transactions);
        Assertions.assertEquals(transactions, transactionService.findAll());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void findAllWithTransactionsWhichHaveZeroTransaction() {
        when(transactionRepository.findAll()).thenReturn(emptyTransactions);
        Assertions.assertEquals(emptyTransactions, transactionService.findAll());
        verify(transactionRepository, times(1)).findAll();
    }

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public TransactionService transactionService() {
            return new TransactionServiceImpl();
        }
    }
}
