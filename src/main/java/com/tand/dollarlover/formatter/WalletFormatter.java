package com.tand.dollarlover.formatter;

import com.tand.dollarlover.model.Wallet;
import com.tand.dollarlover.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class WalletFormatter implements Formatter<Wallet> {

    private WalletService walletService;

    @Autowired
    public WalletFormatter(WalletService walletService){
        this.walletService = walletService;
    }
    @Override
    public Wallet parse(String text, Locale locale) throws ParseException {
        return walletService.findById((long) Integer.parseInt(text));
    }

    @Override
    public String print(Wallet object, Locale locale) {
        return null;
    }
}
