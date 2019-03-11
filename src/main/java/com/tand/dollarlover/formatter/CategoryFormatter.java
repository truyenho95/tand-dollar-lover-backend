package com.tand.dollarlover.formatter;

import com.tand.dollarlover.model.Category;
import com.tand.dollarlover.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

public class CategoryFormatter implements Formatter<Optional<Category>> {
    private CategoryService categoryService;

    @Autowired
    public CategoryFormatter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Optional<Category> parse(String text, Locale locale) throws ParseException {
        return categoryService.findById((long) Integer.parseInt(text));
    }


    @Override
    public String print(Optional<Category> object, Locale locale) {
        return null;
    }
}
