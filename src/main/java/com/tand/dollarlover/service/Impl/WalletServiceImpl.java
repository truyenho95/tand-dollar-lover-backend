package com.tand.dollarlover.service.Impl;

import com.tand.dollarlover.model.Wallet;
import com.tand.dollarlover.repository.WalletRepository;
import com.tand.dollarlover.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("WalletService")
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Optional<Wallet> findById(Long id) {
        return walletRepository.findById(id);
    }

    @Override
    public Iterable<Wallet> findAll() {
        return walletRepository.findAll();
    }

    @Override
    public void save(Optional<Wallet> wallet) {
        walletRepository.save(wallet.get());
    }

    @Override
    public void remove(Long id) {
        walletRepository.deleteById(id);
    }
}
