package com.tand.dollarlover.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tand.dollarlover.model.Wallet;
import com.tand.dollarlover.service.Impl.WalletServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(WalletController.class)
@SpringJUnitConfig(WalletControllerTestConfig.class)

public class WalletControllerTest {

    private static Long sampleId;
    private static String sampleName;
    private static Double sampleOpeningBalance;
    private static Wallet emptyWallet;
    private static Wallet sampleWallet;

    static {
        sampleId = 1L;
        sampleName = "Sample Name";
        sampleOpeningBalance = 10000d;

        emptyWallet = Wallet.builder().build();

        sampleWallet = Wallet.builder()
                .id(sampleId)
                .name(sampleName)
                .openingBalance(sampleOpeningBalance)
                .build();
    }

    @Autowired
    WebApplicationContext webApplicationContext;
    private List<Wallet> wallets;

    @Autowired
    private MockMvc mvc;
    @MockBean
    private WalletController walletController;
    @MockBean
    private WalletServiceImpl walletService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(walletController).build();
/*
        Wallet wallet1 = Wallet.builder()
                .name("Test OK")
                .openingBalance(10000)
                .build();
        wallets = new ArrayList<>();
        wallets.add(wallet1);*/
    }

    protected String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    public void givenWallets_whenGetWallets_thenResponseOK() throws Exception {
        mvc.perform(get("/wallets"))
                .andExpect(status().isOk());
    }


    @Test
    public void getWalletList() throws Exception {
        // when(walletService.findAll()).thenReturn(wallets);

        String uri = "/wallets";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("[{'id': 1,'name': 'test';'openingBalance': 10000.0}]"))
                .andReturn();


      /*  String content = mvcResult.getResponse().getContentAsString();
        Wallet[] wallets = mapFromJson(content, Wallet[].class);*/

        //Assertions.assertTrue(wallets.length > 0);


    }

}
