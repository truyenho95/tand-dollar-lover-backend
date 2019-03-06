package com.tand.dollarlover.repository;

import com.tand.dollarlover.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*",maxAge = 3600)
@Repository("TransactionRepository")
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
