package com.tand.dollarlover.service;

import com.tand.dollarlover.model.Wallet;

public interface WalletService {

    Wallet findById(Long id);

    Iterable<Wallet> findAll();

    void save(Wallet wallet);

    void remove(Long id);

}
