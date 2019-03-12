package com.tand.dollarlover.controller;


import com.tand.dollarlover.model.Category;
import com.tand.dollarlover.service.Impl.CategoryServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
@SpringJUnitConfig(CategoryControllerTestConfig.class)

public class CategoryControllerTest {
    static {
        Long sampleId = 1L;
        String sampleName = "Sample Name";
        Boolean sampleIsIncome = true;

        Category emptyCategory = Category.builder().build();

        Category sampleCategory = Category.builder()
                .id(sampleId)
                .name(sampleName)
                .isIncome(sampleIsIncome)
                .build();
    }

    private List<Category> categories;

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CategoryController categoryController;
    @MockBean
    private CategoryServiceImpl categoryService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        Category category1 = Category.builder()
                .id(1L)
                .isIncome(true)
                .name("Sample Category")
                .build();
        categories = new ArrayList<>();
        categories.add(category1);

        categoryService.save(Optional.ofNullable((category1)));
    }

    @Before
    public void createCategoryIfNotExist() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String getOrCreateURL = "http://localhost:" + 8080 + "/categories";
        URI uriCategory = new URI(getOrCreateURL);

        ResponseEntity<String> responseCategory = restTemplate.getForEntity(uriCategory, String.class);

        if (responseCategory.getBody().startsWith("[]")) {
            Category category = Category.builder()
                    .name("Market")
                    .isIncome(true)
                    .build();
            HttpEntity<Category> request = new HttpEntity<>(category);
            restTemplate.postForEntity(uriCategory, request, String.class);
        }
    }

    @Test
    public void testDummy() {
        Assertions.assertEquals(2, 1 + 1);
    }

    @Test
    public void givenCategories_whenGetCategories_thenResponseOK() throws Exception {
        mvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllCategoryList() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + 8080 + "/categories";
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
    public void testGetCategoryById() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + 8080 + "/categories";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        String result = responseEntity.getBody().replace(",", " ");
        Long idForGet = Long.valueOf(result.substring(7, 9).trim());

        final String urlGet = "http://localhost:" + 8080 + "/categories/" + idForGet;
        URI uriGet = new URI(urlGet);

        ResponseEntity<String> getById = restTemplate.getForEntity(uriGet, String.class);

        Assertions.assertEquals(200, getById.getStatusCodeValue());
        Assertions.assertFalse(getById.getBody().isEmpty());

        JSONObject obj = new JSONObject(getById.getBody());
        System.out.println(obj);
    }

    @Test
    public void testCreateCategory() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + 8080 + "/categories";
        URI uri = new URI(baseUrl);

        Category category = new Category("test create", true);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Category> request = new HttpEntity<>(category, headers);
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
    public void testUpdateCategory() throws Exception {

        final String getOrCreateURL = "http://localhost:" + 8080 + "/categories";
        URI uriGetOrCreate = new URI(getOrCreateURL);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriGetOrCreate, String.class);

        String replaceCommaWithSpace = restTemplate.getForEntity(uriGetOrCreate, String.class).getBody().replace(",", " ");
        Long idForUpdate = Long.valueOf(replaceCommaWithSpace.substring(7, 9).trim());


        final String updateURL = "http://localhost:" + 8080 + "/categories/" + idForUpdate;
        URI uriUpdate = new URI(updateURL);

        Category updatedCategory = new Category(idForUpdate, "updated", true);

        HttpEntity<Category> requestUpdate = new HttpEntity<>(updatedCategory);

        try {
            restTemplate.exchange(uriUpdate, HttpMethod.PUT, requestUpdate, Void.class);
            Assertions.assertTrue(restTemplate.getForEntity(uriGetOrCreate, String.class).getBody().contains("updated"));
            Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        } catch (HttpClientErrorException ex) {
            Assertions.assertEquals(500, ex.getRawStatusCode());
        }
    }

    @Test
    public void testDeleteCategory() throws Exception {


        final String getOrCreateURL = "http://localhost:" + 8080 + "/categories";
        URI uriGetOrCreate = new URI(getOrCreateURL);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriGetOrCreate, String.class);

        String replaceCommaWithSpace = restTemplate.getForEntity(uriGetOrCreate, String.class).getBody().replace(",", " ");
        Long idForDelete = Long.valueOf(replaceCommaWithSpace.substring(7, 9).trim());

        final String deleteUrl = "http://localhost:" + 8080 + "/categories/" + idForDelete;
        URI uriDelete = new URI(deleteUrl);
        try {
            restTemplate.delete(uriDelete);
            Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        } catch (HttpClientErrorException ex) {
            Assertions.assertEquals(500, ex.getRawStatusCode());
        }
    }
}
