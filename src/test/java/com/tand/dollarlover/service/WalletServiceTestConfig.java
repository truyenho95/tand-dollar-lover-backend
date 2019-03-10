package com.tand.dollarlover.service;

import com.tand.dollarlover.repository.WalletRepository;
import com.tand.dollarlover.service.Impl.WalletServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletServiceTestConfig {
    @Bean
    public WalletRepository walletRepository() {
        return Mockito.mock(WalletRepository.class);
    }

    @Bean
    public WalletService walletService() {
        return new WalletServiceImpl();
    }
}
