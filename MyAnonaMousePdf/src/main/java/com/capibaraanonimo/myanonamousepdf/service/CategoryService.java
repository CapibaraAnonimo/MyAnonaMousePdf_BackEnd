package com.capibaraanonimo.myanonamousepdf.service;

import com.capibaraanonimo.myanonamousepdf.errors.exceptions.SingleEntityNotFoundException;
import com.capibaraanonimo.myanonamousepdf.model.Category;
import com.capibaraanonimo.myanonamousepdf.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public boolean existsById(UUID id) {
        return categoryRepository.existsById(id);
    }

    public Category findById(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent())
            return category.get();
        throw new SingleEntityNotFoundException(id, Category.class);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
