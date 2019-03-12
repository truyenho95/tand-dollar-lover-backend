package com.tand.dollarlover.repository;

import com.tand.dollarlover.model.Transaction;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryTest {
    private static Transaction transaction;
    private static List<Transaction> emptyTransactions;
    private static List<Transaction> transactions;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setupEntityManager() {

    }

    @AfterEach
    void resetMocks() {
        Mockito.reset(entityManagerFactory);
    }

    @Test
    public void findAllWithNoTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        System.out.println(transactions);
        Iterable<Transaction> find = transactionRepository.findAll();
        System.out.println(find);
        Assertions.assertEquals(transactions, find);
    }

    @Test
    public void findAll() {
        Transaction transaction = new Transaction();
        transaction.setAmount(10000);
        transaction = entityManager.persistAndFlush(transaction);
        List<Transaction> testList = new ArrayList<>(Collections.singleton(transaction));

        Iterable<Transaction> find = transactionRepository.findAll();

        Assertions.assertEquals(testList, find);
    }

    @Test
    public void findTransactionById() {
        Transaction transaction = new Transaction();
        transaction.setAmount(10000);

        transaction = entityManager.persistAndFlush(transaction);
        ArrayList<Transaction> testList = new ArrayList<>(Collections.singleton(transaction));
        Optional<Transaction> optional = testList.stream().findAny();

        Optional<Transaction> find = transactionRepository.findById(transaction.getId());

        Assertions.assertEquals(optional, find);
    }
}