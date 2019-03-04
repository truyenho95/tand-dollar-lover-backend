package com.tand.dollarlover.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;



    private double openingBalance;

    @OneToMany(targetEntity = Transaction.class)
    private List<Transaction> transactions;

    public Wallet() {
    }

    public Wallet(String name, double openingBalance) {
        this.name = name;
        this.openingBalance = openingBalance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
