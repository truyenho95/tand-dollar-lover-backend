package com.tand.dollarlover.service;

import com.tand.dollarlover.model.Transaction;

import java.sql.Date;
import java.util.Optional;

public interface TransactionService {

    Iterable<Transaction> findAllByDateBetween(Optional<Date>  timeStart,Optional<Date> timeEnd);


    Iterable<Transaction> findAllByDateBetweenAndWallet_Id(Optional<Date> timeStart,Optional<Date> timeEnd,Optional<Long> walletId);

    Iterable<Transaction> findAll();

    Optional<Transaction> findById(Long id);

    void  save(Transaction transaction);

    void remove(Long id);
}