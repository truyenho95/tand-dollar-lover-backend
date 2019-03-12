package com.tand.dollarlover.controller;


import com.tand.dollarlover.model.Category;
import com.tand.dollarlover.model.Transaction;
import com.tand.dollarlover.model.Wallet;
import com.tand.dollarlover.service.Impl.TransactionServiceImpl;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
@SpringJUnitConfig(TransactionControllerTestConfig.class)
public class TransactionControllerTest {
    static {
        Long sampleId = 1L;
        String sampleDescriptions = "Sample Descriptions";
        Double sampleAmount = 10000d;
        Boolean sampleIsIncome = true;

        Transaction emptyTransaction = Transaction.builder().build();

        Transaction sampleTransaction = Transaction.builder()
                .id(sampleId)
                .amount(sampleAmount)
                .descriptions(sampleDescriptions)
                .build();
    }

    private List<Transaction> transactions;

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TransactionController transactionController;
    @MockBean
    private TransactionServiceImpl transactionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        Transaction transaction1 = Transaction.builder()
                .amount(20000)
                .descriptions("Sample Descriptions")
                .build();
        transactions = new ArrayList<>();
        transactions.add(transaction1);

        transactionService.save(transaction1);
    }

    @Before
    public void createWalletAndCategoryUsingForTransTest() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String baseWalletUrl = "http://localhost:" + 8080 + "/wallets";
        URI uriWallets = new URI(baseWalletUrl);
        final String baseCategoryURL = "http://localhost:" + 8080 + "/categories";
        URI uriCategories = new URI(baseCategoryURL);

        ResponseEntity<String> responseWallet = restTemplate.getForEntity(uriWallets, String.class);
        ResponseEntity<String> responseCategory = restTemplate.getForEntity(uriCategories, String.class);

        if (responseWallet.getBody().startsWith("[]")) {
            Wallet wallet1 = Wallet.builder()
                    .name("test for trans")
                    .balance(10000)
                    .build();
            HttpEntity<Wallet> request = new HttpEntity<>(wallet1);
            restTemplate.postForEntity(uriWallets, request, String.class);
        }
        if (responseCategory.getBody().startsWith("[]")) {
            Category category = Category.builder()
                    .isIncome(true)
                    .name("Market")
                    .build();
            HttpEntity<Category> request = new HttpEntity<>(category);
            restTemplate.postForEntity(uriCategories, request, String.class);
        }
    }

    @Before
    public void createTransIfNotExist() throws Exception {
        Date date = new Date();
        java.sql.Date sqlDate = convertUtilToSql(date);
        RestTemplate restTemplate = new RestTemplate();

        final String getOrCreateURL = "http://localhost:" + 8080 + "/transactions";
        URI uriTrans = new URI(getOrCreateURL);
        final String baseWalletUrl = "http://localhost:" + 8080 + "/wallets";
        URI uriWallets = new URI(baseWalletUrl);
        final String baseCategoryUrl = "http://localhost:" + 8080 + "/categories";
        URI uriCategories = new URI(baseCategoryUrl);

        ResponseEntity<String> responseTrans = restTemplate.getForEntity(uriTrans, String.class);
        ResponseEntity<String> responseWallet = restTemplate.getForEntity(uriWallets, String.class);
        ResponseEntity<String> responseCategory = restTemplate.getForEntity(uriCategories, String.class);

        String replaceCommaWithSpace = responseWallet.getBody().replace(",", " ");
        Long idForGetWallet = Long.valueOf(replaceCommaWithSpace.substring(7, 9).trim());

        String replaceCommaWithSpace1 = responseCategory.getBody().replace(",", " ");
        Long idForGetCategory = Long.valueOf(replaceCommaWithSpace1.substring(7, 9).trim());
        System.out.println(idForGetCategory);

        final String urlGetWallet = "http://localhost:" + 8080 + "/wallets/" + idForGetWallet;
        URI uriGetWallet = new URI(urlGetWallet);
        final String urlGetCategory = "http://localhost:" + 8080 + "/categories/" + idForGetCategory;
        URI uriGetCategory = new URI(urlGetCategory);

        ResponseEntity<Wallet> getWallet = restTemplate.getForEntity(uriGetWallet, Wallet.class);
        ResponseEntity<Category> getCategory = restTemplate.getForEntity(uriGetCategory, Category.class);

        Wallet walletForTrans = getWallet.getBody();
        Category categoryForTrans = getCategory.getBody();

        if (responseTrans.getBody().startsWith("[]")) {
            Transaction transaction = Transaction.builder()
                    .category(categoryForTrans)
                    .amount(10000)
                    .date(sqlDate)
                    .descriptions("test delete")
                    .wallet(walletForTrans)
                    .build();
            HttpEntity<Transaction> request = new HttpEntity<>(transaction);
            restTemplate.postForEntity(uriTrans, request, String.class);
        }
    }


    private java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

    private Wallet getWallet() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        final String baseWalletUrl = "http://localhost:" + 8080 + "/wallets";
        URI uriWallets = new URI(baseWalletUrl);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriWallets, String.class);

        String replaceCommaWithSpace = responseEntity.getBody().replace(",", " ");
        Long idForGetWallet = Long.valueOf(replaceCommaWithSpace.substring(7, 9).trim());

        final String urlGetWallet = "http://localhost:" + 8080 + "/wallets/" + idForGetWallet;
        URI uriGetWallet = new URI(urlGetWallet);

        ResponseEntity<Wallet> getWallet = restTemplate.getForEntity(uriGetWallet, Wallet.class);
        return getWallet.getBody();
    }

    private Category getCategory() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        final String baseCategoryUrl = "http://localhost:" + 8080 + "/categories";
        URI uriCategories = new URI(baseCategoryUrl);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriCategories, String.class);

        String replaceCommaWithSpace = responseEntity.getBody().replace(",", " ");
        Long idForGetCategory = Long.valueOf(replaceCommaWithSpace.substring(7, 9).trim());

        final String urlGetCategory = "http://localhost:" + 8080 + "/categories/" + idForGetCategory;
        URI uriGetCategory = new URI(urlGetCategory);

        ResponseEntity<Category> getCategory = restTemplate.getForEntity(uriGetCategory, Category.class);
        return getCategory.getBody();
    }

    @Test
    public void testDummy() {
        Assertions.assertEquals(2, 1 + 1);
    }


    @Test
    public void givenTransactions_whenGetTransactions_thenResponseOK() throws Exception {
        mvc.perform(get("/transactions"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllTransactionList() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + 8080 + "/transactions";
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
    public void testGetTransactionById() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + 8080 + "/transactions";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        String replaceCommaWithSpace = responseEntity.getBody().replace(",", " ");
        System.out.println(replaceCommaWithSpace);
        Long idForGet = Long.valueOf(replaceCommaWithSpace.substring(7, 9).trim());
        System.out.println(idForGet);

        final String urlGet = "http://localhost:" + 8080 + "/transactions/" + idForGet;
        URI uriGet = new URI(urlGet);

        ResponseEntity<String> getById = restTemplate.getForEntity(uriGet, String.class);

        Assertions.assertEquals(200, getById.getStatusCodeValue());
        Assertions.assertFalse(getById.getBody().isEmpty());

        JSONObject obj = new JSONObject(getById.getBody());
        System.out.println(obj);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Date date = new Date();
        java.sql.Date sqlDate = convertUtilToSql(date);

        final String baseTransUrl = "http://localhost:" + 8080 + "/transactions";
        URI uriTrans = new URI(baseTransUrl);

        Wallet walletForTrans = getWallet();
        Category categoryForTrans = getCategory();

        Transaction transaction = new Transaction(50000, "test create", sqlDate, walletForTrans, categoryForTrans);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Transaction> requestForTrans = new HttpEntity<>(transaction, headers);

        restTemplate.postForEntity(uriTrans, requestForTrans, String.class);

        ResponseEntity<String> resultResponse = restTemplate.getForEntity(uriTrans, String.class);

        try {
            System.out.println(resultResponse.getBody());
            Assertions.assertTrue(resultResponse.getBody().contains("test create"));
            Assertions.assertEquals(200, resultResponse.getStatusCodeValue());
        } catch (HttpClientErrorException ex) {
            Assertions.assertEquals(400, ex.getRawStatusCode());
            Assertions.assertTrue(ex.getResponseBodyAsString().contains("Missing request header"));
        }
    }

    @Test
    public void testDeleteWallet() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Date date = new Date();
        java.sql.Date sqlDate = convertUtilToSql(date);

        final String getOrCreateURL = "http://localhost:" + 8080 + "/transactions";
        URI uriGetOrCreate = new URI(getOrCreateURL);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriGetOrCreate, String.class);

        String replaceCommaWithSpace = restTemplate.getForEntity(uriGetOrCreate, String.class).getBody().replace(",", " ");
        Long idForDelete = Long.valueOf(replaceCommaWithSpace.substring(7, 9).trim());
        String stringForAssert = idForDelete.toString();

        final String deleteUrl = "http://localhost:" + 8080 + "/transactions/" + idForDelete;
        URI uriDelete = new URI(deleteUrl);


        try {
            restTemplate.delete(uriDelete);
            Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        } catch (HttpClientErrorException ex) {
            Assertions.assertEquals(500, ex.getRawStatusCode());
        }
    }

    @Test
    public void testUpdateWallet() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Date date = new Date();
        java.sql.Date sqlDate = convertUtilToSql(date);

        final String getURL = "http://localhost:" + 8080 + "/transactions";
        URI uriGet = new URI(getURL);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriGet, String.class);

        String replaceCommaWithSpace = restTemplate.getForEntity(uriGet, String.class).getBody().replace(",", " ");
        Long idForUpdate = Long.valueOf(replaceCommaWithSpace.substring(7, 9).trim());


        final String updateURL = "http://localhost:" + 8080 + "/transactions/" + idForUpdate;
        URI uriUpdate = new URI(updateURL);

        Wallet walletForUpdate = getWallet();
        Category categoryForTrans = getCategory();

        Transaction updatedTrans = new Transaction(idForUpdate, 9999, "updated", sqlDate, walletForUpdate, categoryForTrans);

        HttpEntity<Transaction> requestUpdate = new HttpEntity<>(updatedTrans);

        try {
            restTemplate.exchange(uriUpdate, HttpMethod.PUT, requestUpdate, Void.class);
            Assertions.assertTrue(restTemplate.getForEntity(uriGet, String.class).getBody().contains("updated"));
            Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        } catch (HttpClientErrorException ex) {
            Assertions.assertEquals(500, ex.getRawStatusCode());
        }
    }

}
