package com.tand.dollarlover.repository;

import com.tand.dollarlover.model.Category;
import com.tand.dollarlover.model.Transaction;
import com.tand.dollarlover.model.Wallet;
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
import java.util.*;

import static org.assertj.core.util.DateUtil.parse;

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

    private java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

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

    @Test
    public void findAllByDateBetween() {
        Date date1 = parse("2019-02-14");
        java.sql.Date sqlDate1 = convertUtilToSql(date1);
        Date date2 = parse("2019-03-14");
        java.sql.Date sqlDate2 = convertUtilToSql(date2);

        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();

        transaction1.setDate(sqlDate1);
        transaction2.setDate(sqlDate2);

        entityManager.persistAndFlush(transaction1);
        entityManager.persistAndFlush(transaction2);

        Iterable<Transaction> find = transactionRepository.findAllByDateBetween(Optional.ofNullable(sqlDate1), Optional.ofNullable(sqlDate2));
        Assertions.assertTrue(find.toString().contains("2019-02-14"));
        Assertions.assertTrue(find.toString().contains("2019-03-14"));
    }

    @Test
    public void findAllByDateBetweenAndWallet_Id() {
        Date date1 = parse("2019-02-14");
        java.sql.Date sqlDate1 = convertUtilToSql(date1);
        Date date2 = parse("2019-03-14");
        java.sql.Date sqlDate2 = convertUtilToSql(date2);

        Wallet wallet1 = new Wallet(1L, "wallet 1", 10000);
        Wallet wallet2 = new Wallet(2L, "wallet 2", 20000);

        entityManager.merge(wallet1);
        entityManager.merge(wallet2);

        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();


        transaction1.setDate(sqlDate1);
        transaction2.setDate(sqlDate2);
        transaction1.setWallet(wallet1);
        transaction2.setWallet(wallet2);


        entityManager.persistAndFlush(transaction1);
        entityManager.persistAndFlush(transaction2);


        Iterable<Transaction> find = transactionRepository.findAllByDateBetweenAndWallet_Id(Optional.ofNullable(sqlDate1), Optional.ofNullable(sqlDate2), Optional.of(1L));
        System.out.println(find);
        Assertions.assertTrue(find.toString().contains("2019-02-14"));
        Assertions.assertTrue(find.toString().contains("1"));
    }

    @Test
    public void findAllByCategoryNameContaining() {
        Category category = new Category("Market", false);
        Transaction transaction = new Transaction();
        transaction.setCategory(category);

        entityManager.persistAndFlush(category);
        entityManager.persistAndFlush(transaction);

        Iterable<Transaction> find = transactionRepository.findAllByCategoryNameContaining(transaction.getCategory().getName());
        Assertions.assertTrue(find.toString().contains("Market"));
    }

    @Test
    public void findAllByCategoryNameContainingAndCategoryIsIncome() {
        Category category = new Category("Salary", true);
        Category category2 = new Category("Market", false);
        Transaction transaction = new Transaction();
        Transaction transaction2 = new Transaction();
        transaction.setCategory(category);
        transaction2.setCategory(category2);

        entityManager.persistAndFlush(category);
        entityManager.persistAndFlush(transaction);
        entityManager.persistAndFlush(category2);
        entityManager.persistAndFlush(transaction2);

        Iterable<Transaction> find = transactionRepository.findAllByCategoryNameContainingAndCategoryIsIncome("Salary", true);
        System.out.println(find);
        Assertions.assertTrue(find.toString().contains("Salary"));
        Assertions.assertFalse(find.toString().contains("Market"));
        Assertions.assertTrue(find.toString().contains("true"));
    }
}