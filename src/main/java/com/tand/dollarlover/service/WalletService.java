package com.tand.dollarlover.service;

import com.tand.dollarlover.model.Wallet;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface WalletService {

    @Transactional
    Optional<Wallet> findById(Long id);

    @Transactional
    Iterable<Wallet> findAll();

    @Transactional
    void save(Optional<Wallet> wallet);

    @Transactional
    void remove(Long id);

}
