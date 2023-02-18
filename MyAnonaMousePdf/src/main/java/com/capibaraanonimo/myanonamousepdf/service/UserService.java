package com.capibaraanonimo.myanonamousepdf.service;

import com.capibaraanonimo.myanonamousepdf.dto.user.CreateUserRequest;
import com.capibaraanonimo.myanonamousepdf.errors.exceptions.SingleEntityNotFoundException;
import com.capibaraanonimo.myanonamousepdf.model.Roles;
import com.capibaraanonimo.myanonamousepdf.model.User;
import com.capibaraanonimo.myanonamousepdf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean userNameExists(String user) {
        return userRepository.existsUserByUsername(user);
    }

    public boolean emailExists(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    public User findById(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent())
            return optionalUser.get();
        else
            throw new SingleEntityNotFoundException(userId, User.class);
    }

    public User findByUsername(String username) {
        Optional<User> optionalUser =  userRepository.findFirstByUsername(username);
        if (optionalUser.isPresent())
            return optionalUser.get();
        throw new UsernameNotFoundException("No user with username: " +  username);
    }

    public User createUser(CreateUserRequest createUserRequest, EnumSet<Roles> roles) {
        User user =  User.builder()
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .avatar(createUserRequest.getAvatar())
                .fullName(createUserRequest.getFullName())
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    public User createUserWithUserRole(CreateUserRequest createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(Roles.USER));
    }

    public User createUserWithAdminRole(CreateUserRequest createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(Roles.ADMIN));
    }
}
