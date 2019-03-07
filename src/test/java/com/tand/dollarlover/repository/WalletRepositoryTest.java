package com.tand.dollarlover.repository;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
//@WebMvcTest(WalletRepository.class)
@DataJpaTest
/*@SpringJUnitConfig(WalletRepositoryTestConfig.class)*/
public class WalletRepositoryTest {
    private static Wallet wallet;
    private static List<Wallet> emptyWallets;
    private static List<Wallet> wallets;

    static {
        wallet = new Wallet("OK", 100);
        emptyWallets = new ArrayList<>();
        wallets = new ArrayList<>();
        wallets.add(wallet);
    }

    /* @Autowired
     private MockMvc mvc;*/
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setupEntityManager() {
        // when(entityManagerFactory.createEntityManager()).thenReturn(em);
    }

    @AfterEach
    void resetMocks() {
        Mockito.reset(entityManagerFactory);
        // Mockito.reset(em);
    }
    @Test
    public void findAllWith0Wallet() {
        /*when(walletRepository.findAll()).thenReturn(emptyWallets);
        Assertions.assertEquals(emptyWallets, walletRepository.findAll());
        verify(walletRepository).findAll();*/

        List<Wallet> wallets = new ArrayList<>();
        System.out.println(wallets);
        Iterable<Wallet> find = walletRepository.findAll();
        System.out.println(find);
        Assertions.assertEquals(wallets, find);
    }

    @Test
    public void findAll() {
        Wallet wallet = new Wallet();
        wallet.setName("test OK");

        wallet = entityManager.persistAndFlush(wallet);
        List<Wallet> testList = new ArrayList<>(Collections.singleton(wallet));

        Iterable<Wallet> find = walletRepository.findAll();

        Assertions.assertEquals(testList, find);
    }

    @Test
    public void findWalletById() {
        Wallet wallet = new Wallet();
        wallet.setName("test ID");

        wallet = entityManager.persistAndFlush(wallet);
        ArrayList<Wallet> testList = new ArrayList<>(Collections.singleton(wallet));
        Optional<Wallet> optional = testList.stream().findAny();

        Optional<Wallet> find = walletRepository.findById(1L);

        Assertions.assertEquals(optional, find);
    }
}
