package com.tand.dollarlover.controller;

import com.tand.dollarlover.model.Wallet;
import com.tand.dollarlover.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @RequestMapping(value = "/wallets", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Wallet>> getAllWallets() {
        Iterable<Wallet> wallets = walletService.findAll();
        if (wallets == null) {
            return new ResponseEntity<Iterable<Wallet>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Iterable<Wallet>>(wallets, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallets/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wallet> getWallet(@PathVariable("id") long id) {
        System.out.println("Fetching Wallet with id " + id);
        Wallet wallet = walletService.findById(id);
        if (wallet == null) {
            System.out.println("Wallet with id " + id + " not found");
            return new ResponseEntity<Wallet>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Wallet>(wallet, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallets", method = RequestMethod.POST)
    public ResponseEntity<Void> createWallet(@RequestBody Wallet wallet, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Wallet " + wallet.getId());
        System.out.println("Creating Wallet " + wallet.getName());

        walletService.save(wallet);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/wallets/{id}").buildAndExpand(wallet.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/wallets/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Wallet> updateWallet(@PathVariable("id") long id, @RequestBody Wallet wallet) {
        System.out.println("Updating Wallet " + id);

        Wallet currentWallet = walletService.findById(id);

        if (wallet == null) {
            System.out.println("Wallet with id " + id + " not found");
            return new ResponseEntity<Wallet>(HttpStatus.NOT_FOUND);
        }

        currentWallet.setName(wallet.getName());
        currentWallet.setOpeningBalance(wallet.getOpeningBalance());
        currentWallet.setId(wallet.getId());

        walletService.save(currentWallet);
        return new ResponseEntity<Wallet>(currentWallet, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallets/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Wallet> deleteWallet(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Wallet with id " + id);

        Wallet wallet = walletService.findById(id);
        if (wallet == null) {
            System.out.println("Unable to delete. Wallet with id " + id + " not found");
            return new ResponseEntity<Wallet>(HttpStatus.NOT_FOUND);
        }

        walletService.remove(id);
        return new ResponseEntity<Wallet>(HttpStatus.NO_CONTENT);
    }
}
