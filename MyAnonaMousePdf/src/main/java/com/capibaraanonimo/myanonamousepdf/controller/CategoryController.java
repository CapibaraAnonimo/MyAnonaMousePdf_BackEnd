package com.capibaraanonimo.myanonamousepdf.controller;

import com.capibaraanonimo.myanonamousepdf.dto.CategoryResponse;
import com.capibaraanonimo.myanonamousepdf.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/category")
@RequiredArgsConstructor()
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryResponse> getAll() {
        return categoryService.findAllWithBooks();
    }
}
