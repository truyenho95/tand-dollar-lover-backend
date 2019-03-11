package com.tand.dollarlover.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private double openingBalance;

    @OneToMany(targetEntity = Transaction.class)
    private List<Transaction> transactions;

    public Wallet(Long id, String name, double openingBalance) {
        this.id = id;
        this.name = name;
        this.openingBalance = openingBalance;
    }
  
    public Wallet(String name, double openingBalance) {
        this.name = name;
        this.openingBalance = openingBalance;
    }

    public Wallet(String name, double openingBalance, List<Transaction> transactions) {
        this.name = name;
        this.openingBalance = openingBalance;
        this.transactions = transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
