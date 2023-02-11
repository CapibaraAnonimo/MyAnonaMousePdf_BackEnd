package com.capibaraanonimo.myanonamousepdf.repository;

import com.capibaraanonimo.myanonamousepdf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public boolean existsUserByUsername(String username);

    public boolean existsUserByEmail(String email);
}
