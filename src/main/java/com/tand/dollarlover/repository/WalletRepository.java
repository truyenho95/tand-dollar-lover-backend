package com.tand.dollarlover.repository;

import com.tand.dollarlover.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin(origins = "*", maxAge = 3600)
@Repository("WalletRepository")
public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
