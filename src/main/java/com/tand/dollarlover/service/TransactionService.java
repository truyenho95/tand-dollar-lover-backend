package com.tand.dollarlover.service;

import com.tand.dollarlover.model.Transaction;

public interface TransactionService {

    Iterable<Transaction> findAll();

    Transaction findById(Long id);

    void  save(Transaction transaction);

    void remove(Long id);
}
