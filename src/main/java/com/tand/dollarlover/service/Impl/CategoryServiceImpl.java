package com.tand.dollarlover.service.Impl;

import com.tand.dollarlover.model.Category;
import com.tand.dollarlover.repository.CategoryRepository;
import com.tand.dollarlover.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("CategoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(Optional<Category> category) {
        categoryRepository.save(category.get());
    }

    @Override
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }
}
