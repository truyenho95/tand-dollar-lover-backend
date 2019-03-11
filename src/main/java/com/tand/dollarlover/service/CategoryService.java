package com.tand.dollarlover.service;

import com.tand.dollarlover.model.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CategoryService {

    @Transactional
    Optional<Category> findById(Long id);

    @Transactional
    Iterable<Category> findAll();

    @Transactional
    void save(Optional<Category> category);

    @Transactional
    void remove(Long id);

}
