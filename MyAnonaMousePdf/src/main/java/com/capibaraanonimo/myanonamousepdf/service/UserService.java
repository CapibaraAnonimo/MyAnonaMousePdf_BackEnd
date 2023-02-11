package com.capibaraanonimo.myanonamousepdf.service;

import com.capibaraanonimo.myanonamousepdf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean userNameExists(String user) {
        return userRepository.existsUserByUsername(user);
    }

    public boolean emailExists(String email) {
        return userRepository.existsUserByEmail(email);
    }
}
