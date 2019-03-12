package com.tand.dollarlover.repository;

import com.tand.dollarlover.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Date;
import java.util.Optional;


@CrossOrigin(origins = "*",maxAge = 3600)
@Repository("TransactionRepository")
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    Iterable<Transaction> findAllByDateBetweenAndWallet_Id(Optional<Date> timeStart,Optional<Date> timeEnd,Optional<Long> walletId);

    Iterable<Transaction> findAllByDateBetween(Optional<Date> timeStart,Optional<Date>  timeEnd);

    Iterable<Transaction> findAllByCategoryNameContainingAndCategoryIsIncome(String category_name, Boolean category_income);

    Iterable<Transaction> findAllByCategoryNameContaining(String category_name);
}
