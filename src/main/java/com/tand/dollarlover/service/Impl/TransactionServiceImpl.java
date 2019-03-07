package com.tand.dollarlover.service.Impl;

import com.tand.dollarlover.model.Transaction;
import com.tand.dollarlover.repository.TransactionRepository;
import com.tand.dollarlover.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service("TransactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public Iterable<Transaction> findAllByDateBetween(Optional<Date>  timeStart,Optional<Date>  timeEnd) {
        return transactionRepository.findAllByDateBetween(timeStart,timeEnd);
    }

    @Override
    public Iterable<Transaction> findAllByDateBetweenAndWallet_Id(Optional<Date> timeStart, Optional<Date> timeEnd, Optional<Long> walletId) {
        return transactionRepository.findAllByDateBetweenAndWallet_Id(timeStart,timeEnd,walletId);
    }


    @Override
    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).get();
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);

    }

    @Override
    public void remove(Long id) {
            transactionRepository.deleteById(id);
    }
}
