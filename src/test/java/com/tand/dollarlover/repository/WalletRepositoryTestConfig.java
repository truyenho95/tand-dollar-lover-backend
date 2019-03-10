package com.tand.dollarlover.repository;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Configuration
public class WalletRepositoryTestConfig {

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return Mockito.mock(EntityManager.class);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Mockito.mock(EntityManagerFactory.class);
    }
}
