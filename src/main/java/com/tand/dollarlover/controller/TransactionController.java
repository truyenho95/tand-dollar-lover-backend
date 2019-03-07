package com.tand.dollarlover.controller;

import com.tand.dollarlover.model.Transaction;
import com.tand.dollarlover.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Date;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/transactions",method = RequestMethod.GET)
    public ResponseEntity<Iterable<Transaction>> getAllTransactions
            (
            @RequestParam("timeStart") Optional<Date>  timeStart,
            @RequestParam("timeEnd") Optional<Date> timeEnd,
            @RequestParam("walletId") Optional<Long> walletId
            )
    {

        Iterable<Transaction> transactions;
        if (timeStart.isPresent())
        {
            if(walletId.isPresent()){
                transactions = transactionService.findAllByDateBetweenAndWallet_Id(timeStart,timeEnd,walletId);
            }
            else{
                  transactions = transactionService.findAllByDateBetween(timeStart,timeEnd);
            }
        }
        else {
            transactions = transactionService.findAll();
        }


        if (transactions == null)
        {
            return new ResponseEntity<Iterable<Transaction>>(HttpStatus.NO_CONTENT);
        }
            return new ResponseEntity<Iterable<Transaction>>(transactions, HttpStatus.OK);
        }

//        timeStart.toLocalDate();

//        if (timeStart.isPresent())
//        {
//            Iterable<Transaction> transactions = transactionService.findAllByDateBetweenAndWallet_Id(timeStart,timeEnd,walletId);
//            if (transactions == null) {
//                return new ResponseEntity<Iterable<Transaction>>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<Iterable<Transaction>>(transactions, HttpStatus.OK);
//        }
//        else {
//            Iterable<Transaction> transactions = transactionService.findAll();
//            if (transactions == null) {
//                return new ResponseEntity<Iterable<Transaction>>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<Iterable<Transaction>>(transactions, HttpStatus.OK);
//        }

//        Iterable<Transaction> transactions = transactionService.findAll();
//        if (transactions == null) {
//            return new ResponseEntity<Iterable<Transaction>>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<Iterable<Transaction>>(transactions, HttpStatus.OK);


    @RequestMapping(value = "/transactions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> getTransaction(@PathVariable("id") long id) {
        System.out.println("Fetching Transaction with id " + id);
        Transaction transaction = transactionService.findById(id);
        if (transaction == null) {
            System.out.println("Transaction with id " + id + " not found");
            return new ResponseEntity<Transaction>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Void> createTransaction(@RequestBody Transaction transaction, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Transaction " + transaction.getId());
        System.out.println("Creating Transaction in Wallet " + transaction.getWallet().getName());

        transactionService.save(transaction);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/transactions/{id}").buildAndExpand(transaction.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/transactions/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") long id, @RequestBody Transaction transaction) {
        System.out.println("Updating Transaction " + id);

        Transaction currentTransaction = transactionService.findById(id);

        if (transaction == null) {
            System.out.println("Transaction with id " + id + " not found");
            return new ResponseEntity<Transaction>(HttpStatus.NOT_FOUND);
        }


        currentTransaction.setId(transaction.getId());
        currentTransaction.setAmount(transaction.getAmount());
        currentTransaction.setDate(transaction.getDate());
        currentTransaction.setDescriptions(transaction.getDescriptions());
        currentTransaction.setIncome(transaction.isIncome());
        currentTransaction.setWallet(transaction.getWallet());

        transactionService.save(currentTransaction);
        return new ResponseEntity<Transaction>(currentTransaction, HttpStatus.OK);
    }

    @RequestMapping(value = "/transactions/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Transaction with id " + id);

        Transaction transaction = transactionService.findById(id);
        if (transaction == null) {
            System.out.println("Unable to delete. Transaction with id " + id + " not found");
            return new ResponseEntity<Transaction>(HttpStatus.NOT_FOUND);
        }

        transactionService.remove(id);
        return new ResponseEntity<Transaction>(HttpStatus.NO_CONTENT);
    }
}
