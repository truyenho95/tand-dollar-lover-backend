package com.tand.dollarlover.repository;

import com.tand.dollarlover.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("WalletRepository")
public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
