package com.tand.dollarlover.controller;

import com.tand.dollarlover.service.WalletService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WalletController.class)
public class TestWalletControllerControler {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private WalletService walletService;

    @Test
    public void givenWallets_whenGetWallets_thenResponseOK() throws Exception {
        mvc.perform(get("/wallets"))
                .andExpect(status().isOk());
    }
}
