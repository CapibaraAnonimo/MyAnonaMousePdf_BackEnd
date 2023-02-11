package com.capibaraanonimo.myanonamousepdf.repository;

import com.capibaraanonimo.myanonamousepdf.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
