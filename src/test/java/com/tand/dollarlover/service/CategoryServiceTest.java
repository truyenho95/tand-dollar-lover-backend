package com.tand.dollarlover.service;


import com.tand.dollarlover.model.Category;
import com.tand.dollarlover.repository.CategoryRepository;
import com.tand.dollarlover.service.Impl.CategoryServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@WebMvcTest(CategoryService.class)
public class CategoryServiceTest {
    private static Category category;
    private static Page<Category> categoryList;
    private static List<Category> categories;
    private static List<Category> emptyCategory;

    static {
        category = new Category("OK", false);
        category.setId(1L);
        categories = Arrays.asList(category);
        categoryList = new PageImpl<>(categories);
        //categories.add(category);
    }

    @Autowired
    private CategoryServiceImpl categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    @AfterEach
    private void resetMocks() {
        Mockito.reset(categoryRepository);
    }

    @Test
    public void testDummy() {
        Assertions.assertEquals(2, 1 + 1);
    }

    @Test
    public void save() {
        categoryService.save(Optional.ofNullable((category)));
        verify(categoryRepository, atLeastOnce()).save(categoryCaptor.capture());
        Assertions.assertEquals(categoryCaptor.getValue().getName(), "OK");
    }

    @Test
    public void delete() {
        categoryService.remove(category.getId());
        verify(categoryRepository, atLeastOnce()).deleteById(1L);
    }

    @Test
    public void findAllWith1Wallet() {
        when(categoryRepository.findAll()).thenReturn(categories);
        Assertions.assertEquals(categories, categoryService.findAll());
        verify(categoryRepository, atLeastOnce()).findAll();

    }

    @Test
    public void findAllWith0Wallet() {
        when(categoryRepository.findAll()).thenReturn(emptyCategory);
        Assertions.assertEquals(emptyCategory, categoryService.findAll());
        verify(categoryRepository, atLeastOnce()).findAll();
    }

    @Test
    public void findByIdFound() {
        Long id = 1l;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        Assertions.assertEquals(Optional.of(category), categoryService.findById(id));
        verify(categoryRepository, atLeastOnce()).findById(id);
    }

    @Test
    public void findByIdNotFound() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(null);
        Assertions.assertNull(categoryService.findById(id));
        verify(categoryRepository, atLeastOnce()).findById(id);
    }
}
