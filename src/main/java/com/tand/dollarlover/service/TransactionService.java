package com.tand.dollarlover.service;

import com.tand.dollarlover.model.Transaction;

import java.util.Optional;

public interface TransactionService {

    Iterable<Transaction> findAll();

    Optional<Transaction> findById(Long id);

    void  save(Transaction transaction);

    void remove(Long id);
}
