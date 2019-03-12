package com.tand.dollarlover.repository;


import com.tand.dollarlover.model.Category;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryTest {
    static {
        Category category = new Category("OK", true);
        List<Category> emptyCategories = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        categories.add(category);
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    @AfterEach
    void resetMock() {
        Mockito.reset(entityManagerFactory);
        Mockito.reset(entityManager);
    }

    @Test
    public void findAllWith0Category() {
        List<Category> categories = new ArrayList<>();
        Iterable<Category> find = categoryRepository.findAll();
        Assertions.assertEquals(categories, find);
    }

    @Test
    public void findAll() {
        Category category = new Category();
        category.setName("OK");

        category = entityManager.persistAndFlush(category);
        List<Category> testList = new ArrayList<>(Collections.singleton(category));

        Iterable<Category> find = categoryRepository.findAll();

        Assertions.assertEquals(testList, find);
    }

    @Test
    public void findCategoryById() {
        Category category = new Category();
        category.setName("test ID");

        category = entityManager.persistAndFlush(category);
        ArrayList<Category> testList = new ArrayList<>(Collections.singleton(category));
        Optional<Category> optionalCategory = testList.stream().findAny();

        Optional<Category> find = categoryRepository.findById(category.getId());

        Assertions.assertEquals(optionalCategory, find);
    }

}
