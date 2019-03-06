package com.tand.dollarlover.repository;

import com.tand.dollarlover.model.Wallet;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(WalletRepository.class)
@SpringJUnitConfig(WalletRepositoryTestConfig.class)
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

    @Autowired
    private MockMvc mvc;
    @MockBean
    private WalletRepository walletRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setupEntityManager() {
        when(entityManagerFactory.createEntityManager()).thenReturn(em);
    }

    @AfterEach
    void resetMocks() {
        Mockito.reset(entityManagerFactory);
        Mockito.reset(em);
    }
    @Test
    public void findAllWith0Wallet() {
        when(walletRepository.findAll()).thenReturn(emptyWallets);
        Assertions.assertEquals(emptyWallets, walletRepository.findAll());
        verify(walletRepository).findAll();
    }

    @Test
    public void findAll() {
        when(walletRepository.findAll()).thenReturn(wallets);
        Assertions.assertNotNull(wallets);
        Assertions.assertEquals(wallets, walletRepository.findAll());
    }

    @Test
    public void findWalletById() {
/*        Long id = 1L;
        //Optional<Wallet> wallets = walletRepository.findById(id);
        wallets.add(wallet);*/
        //when(walletRepository.findById(1L)).thenReturn(Optional.of(new Wallet("OK", 100)));
        Optional<Wallet> find = walletRepository.findById(1L);


        Assertions.assertNotNull(find);
        Assertions.assertEquals("OK", find.get().getName());
        Assertions.assertEquals(100, find.get().getOpeningBalance());

    }



}
