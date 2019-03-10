package com.tand.dollarlover.controller;

import com.tand.dollarlover.model.Wallet;
import com.tand.dollarlover.service.Impl.WalletServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(WalletController.class)
@SpringJUnitConfig(WalletControllerTestConfig.class)
public class WalletControllerTest {

    static {
        Long sampleId = 1L;
        String sampleName = "Sample Name";
        Double sampleOpeningBalance = 10000d;

        Wallet emptyWallet = Wallet.builder().build();

        Wallet sampleWallet = Wallet.builder()
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
        Wallet wallet1 = Wallet.builder()
                .name("Test OK")
                .openingBalance(20000)
                .build();
        wallets = new ArrayList<>();
        wallets.add(wallet1);

        walletService.save(Optional.ofNullable(wallet1));
    }

    @Test
    public void testDummy() {
        Assertions.assertEquals(2, 1 + 1);
    }

    @Test
    public void givenWallets_whenGetWallets_thenResponseOK() throws Exception {
        mvc.perform(get("/wallets"))
                .andExpect(status().isOk());
    }


    @Test
    public void testGetAllWalletList() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + 8080 + "/wallets";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        Assertions.assertEquals(200, result.getStatusCodeValue());
        Assertions.assertFalse(result.getBody().isEmpty());

        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray(result.getBody());
        obj.put("test", array);
        System.out.println(obj);
    }

    @Test
    public void testGetWalletById() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + 8080 + "/wallets";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        String result = responseEntity.getBody().replace(",", " ");
        Long idForGet = Long.valueOf(result.substring(7, 9).trim());

        final String urlGet = "http://localhost:" + 8080 + "/wallets/" + idForGet;
        URI uriGet = new URI(urlGet);

        ResponseEntity<String> getById = restTemplate.getForEntity(uriGet, String.class);

        Assertions.assertEquals(200, getById.getStatusCodeValue());
        Assertions.assertFalse(getById.getBody().isEmpty());

        JSONObject obj = new JSONObject(getById.getBody());
        System.out.println(obj);
    }


    @Test
    public void testCreateWallet() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + 8080 + "/wallets";
        URI uri = new URI(baseUrl);

        Wallet wallet = new Wallet("test create", 1500.0);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Wallet> request = new HttpEntity<>(wallet, headers);
        restTemplate.postForEntity(uri, request, String.class);

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        try {
            System.out.println(result.getBody());
            Assertions.assertTrue(result.getBody().contains("test create"));
            Assertions.assertEquals(200, result.getStatusCodeValue());
        } catch (HttpClientErrorException ex) {
            Assertions.assertEquals(400, ex.getRawStatusCode());
            Assertions.assertTrue(ex.getResponseBodyAsString().contains("Missing request header"));
        }
    }

    @Test
    public void testDeleteWallet() throws Exception {


        final String getOrCreateURL = "http://localhost:" + 8080 + "/wallets";
        URI uriGetOrCreate = new URI(getOrCreateURL);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriGetOrCreate, String.class);

        if (responseEntity.getBody().startsWith("[]") || !responseEntity.getBody().contains("test delete")) {
            Wallet wallet1 = Wallet.builder()
                    .name("test delete")
                    .openingBalance(10000)
                    .build();
            HttpEntity<Wallet> request = new HttpEntity<>(wallet1);
            restTemplate.postForEntity(uriGetOrCreate, request, String.class);
        }

        String result = restTemplate.getForEntity(uriGetOrCreate, String.class).getBody().replace(",", " ");
        Long idForDelete = Long.valueOf(result.substring(7, 9).trim());

        final String deleteUrl = "http://localhost:" + 8080 + "/wallets/" + idForDelete;
        URI uriDelete = new URI(deleteUrl);
        try {
            restTemplate.delete(uriDelete);
            Assertions.assertFalse(restTemplate.getForEntity(uriGetOrCreate, String.class).getBody().contains(result));
            Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        } catch (HttpClientErrorException ex) {
            Assertions.assertEquals(500, ex.getRawStatusCode());
        }
    }

    @Test
    public void testUpdateWallet() throws Exception {

        final String getOrCreateURL = "http://localhost:" + 8080 + "/wallets";
        URI uriGetOrCreate = new URI(getOrCreateURL);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriGetOrCreate, String.class);

        if (responseEntity.getBody().startsWith("[]") || !responseEntity.getBody().contains("test update")) {
            Wallet wallet1 = Wallet.builder()
                    .name("test update")
                    .openingBalance(10000)
                    .build();
            HttpEntity<Wallet> request = new HttpEntity<>(wallet1);
            restTemplate.postForEntity(uriGetOrCreate, request, String.class);
        }


        String result = restTemplate.getForEntity(uriGetOrCreate, String.class).getBody().replace(",", " ");
        Long idForUpdate = Long.valueOf(result.substring(7, 9).trim());


        final String updateURL = "http://localhost:" + 8080 + "/wallets/" + idForUpdate;
        URI uriUpdate = new URI(updateURL);

        Wallet updatedWallet = new Wallet(idForUpdate, "updated", 123);

        HttpEntity<Wallet> requestUpdate = new HttpEntity<>(updatedWallet);

        try {
            restTemplate.exchange(uriUpdate, HttpMethod.PUT, requestUpdate, Void.class);
            Assertions.assertTrue(restTemplate.getForEntity(uriGetOrCreate, String.class).getBody().contains("updated"));
            Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        } catch (HttpClientErrorException ex) {
            Assertions.assertEquals(500, ex.getRawStatusCode());
        }
    }
}
