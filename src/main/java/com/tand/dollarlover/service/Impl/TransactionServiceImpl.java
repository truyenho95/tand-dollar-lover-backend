package com.tand.dollarlover.service.Impl;

import com.tand.dollarlover.model.Transaction;
import com.tand.dollarlover.repository.TransactionRepository;
import com.tand.dollarlover.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("TransactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
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
