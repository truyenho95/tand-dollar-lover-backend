package com.tand.dollarlover.service;

import com.tand.dollarlover.model.Wallet;
import com.tand.dollarlover.repository.WalletRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(WalletService.class)
@SpringJUnitConfig(WalletServiceTestConfig.class)
public class WalletServiceTest {
    private static Wallet wallet;
    private static Page<Wallet> walletList;
    private static List<Wallet> wallets;
    private static List<Wallet> emptyWallets;

    static {
        wallet = new Wallet("OK", 100);
        wallet.setId(1L);
        wallets = Arrays.asList(wallet);
        walletList = new PageImpl<>(wallets);
        //wallets.add(wallet);
    }

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Captor
    private ArgumentCaptor<Wallet> walletCaptor;

    @AfterEach
    private void resetMocks() {
        Mockito.reset(walletRepository);
    }

    @Test
    public void testDummy() {
        Assertions.assertEquals(2, 1 + 1);
    }

    @Test
    public void save() {
        walletService.save(Optional.ofNullable(wallet));
        verify(walletRepository, times(1)).save(walletCaptor.capture());
        Assertions.assertEquals(walletCaptor.getValue().getName(), "OK");
        //assertThat(walletCaptor.getValue().getName(), is(notNullValue()));
    }

    @Test
    public void delete() {
        walletService.remove(wallet.getId());
        verify(walletRepository, times(1)).deleteById(1L);
    }

    @Test
    public void findAllWith1Wallet() {
        when(walletRepository.findAll()).thenReturn(wallets);
        Assertions.assertEquals(wallets, walletService.findAll());
        verify(walletRepository, times(1)).findAll();

    }

    @Test
    public void findAllWith0Wallet() {
        when(walletRepository.findAll()).thenReturn(emptyWallets);
        verify(walletRepository, times(1)).findAll();
        Assertions.assertEquals(emptyWallets, walletService.findAll());
    }

    @Test
    public void findByIdFound() {
        Long id = 1l;
        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));
        Assertions.assertEquals(Optional.of(wallet), walletService.findById(id));
        verify(walletRepository, times(1)).findById(id);
    }

    @Test
    public void findByIdNotFound() {
        Long id = 1L;
        when(walletRepository.findById(id)).thenReturn(null);
        verify(walletRepository).findById(id);
        Assertions.assertNull(walletRepository.findById(id));
    }
}
