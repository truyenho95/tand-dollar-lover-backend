package com.tand.dollarlover.controller;


import com.tand.dollarlover.model.Category;
import com.tand.dollarlover.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Category>> getAllCategories() {
        Iterable<Category> categories = categoryService.findAll();
        if (categories == null) {
            return new ResponseEntity<Iterable<Category>>((HttpStatus.NO_CONTENT));
        }
        return new ResponseEntity<Iterable<Category>>(categories, HttpStatus.OK);
    }

    @GetMapping(value = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id) {
        System.out.println("Fetching Category with id " + id);
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            System.out.println("Category with id " + id + " not found");
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Category>(category.get(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/categories")
    public ResponseEntity<Void> createCategory(@RequestBody Category category, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Category " + category.getId());
        System.out.println("Creating Category " + category.getName());

        categoryService.save(Optional.of(category));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/categories/{id}").buildAndExpand(category.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        System.out.println("Updating Category " + id);

        Optional<Category> currentCategory = categoryService.findById(id);
        if (category == null) {
            System.out.println("Category with id " + id + " not found");
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        }

        currentCategory.get().setId(category.getId());
        currentCategory.get().setName(category.getName());
        currentCategory.get().setIncome(category.getIsIncome());
        //currentCategory.get().setTransactions(category.getTransactions());

        categoryService.save(currentCategory);
        return new ResponseEntity<Category>(currentCategory.get(), HttpStatus.OK);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("id") Long id) {
        System.out.println("Fetching & Deleting Wallet with id " + id);

        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            System.out.println("Unable to delete. Category with id " + id + " not found");
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        }

        categoryService.remove(id);
        return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
    }
}
