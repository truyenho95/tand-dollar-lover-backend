package com.tand.dollarlover.formatter;

import com.tand.dollarlover.model.Wallet;
import com.tand.dollarlover.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

public class WalletFormatter implements Formatter<Optional<Wallet>> {

    private WalletService walletService;

    @Autowired
    public WalletFormatter(WalletService walletService){
        this.walletService = walletService;
    }
    @Override
    public Optional<Wallet> parse(String text, Locale locale) throws ParseException {
        return walletService.findById((long) Integer.parseInt(text));
    }

    @Override
    public String print(Optional<Wallet> object, Locale locale) {
        return null;
    }
}
