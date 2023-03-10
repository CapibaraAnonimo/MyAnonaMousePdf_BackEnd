package com.capibaraanonimo.myanonamousepdf.controller;

import com.capibaraanonimo.myanonamousepdf.dto.book.BookResponse;
import com.capibaraanonimo.myanonamousepdf.dto.user.*;
import com.capibaraanonimo.myanonamousepdf.model.User;
import com.capibaraanonimo.myanonamousepdf.security.jwt.access.JwtProvider;
import com.capibaraanonimo.myanonamousepdf.security.jwt.refresh.RefreshToken;
import com.capibaraanonimo.myanonamousepdf.security.jwt.refresh.RefreshTokenException;
import com.capibaraanonimo.myanonamousepdf.security.jwt.refresh.RefreshTokenRequest;
import com.capibaraanonimo.myanonamousepdf.security.jwt.refresh.RefreshTokenService;
import com.capibaraanonimo.myanonamousepdf.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verify)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtProvider.generateToken(user);
                    refreshTokenService.deleteByUser(user);
                    RefreshToken refreshToken2 = refreshTokenService.createRefreshToken(user);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(JwtUserResponse.builder()
                                    .token(token)
                                    .refreshToken(refreshToken2.getToken())
                                    .build());
                })
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> createUserWithUserRole(@RequestBody @Valid CreateUserRequest createUserRequest) {
        System.out.println(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(userService.createUserWithUserRole(createUserRequest)));
    }

    // M??s adelante podemos manejar la seguridad de acceso a esta petici??n

    @PostMapping("/auth/register/admin")
    public ResponseEntity<UserResponse> createUserWithAdminRole(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(userService.createUserWithAdminRole(createUserRequest)));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtUserResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        System.out.println("Se inicia sesi??n");

        // Realizamos la autenticaci??n

        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

        // Una vez realizada, la guardamos en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Devolvemos una respuesta adecuada
        String token = jwtProvider.generateToken(authentication);

        User user = (User) authentication.getPrincipal();

        // Eliminamos el token (si existe) antes de crearlo, ya que cada usuario deber??a tener solamente un token de refresco simult??neo
        refreshTokenService.deleteByUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(JwtUserResponse.of(user, token, refreshToken.getToken()));
    }

    @PutMapping("/user/changePassword")
    public ResponseEntity<UserResponse> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest,
                                                       @AuthenticationPrincipal User loggedUser) {
        // Este c??digo es mejorable.
        // La validaci??n de la contrase??a nueva se puede hacer con un validador.
        // La gesti??n de errores se puede hacer con excepciones propias
        try {
            if (userService.passwordMatch(loggedUser, changePasswordRequest.getOldPassword())) {
                Optional<User> modified = userService.editPassword(loggedUser.getId(), changePasswordRequest.getNewPassword());
                if (modified.isPresent())
                    return ResponseEntity.ok(UserResponse.fromUser(modified.get()));
            } else {
                // Lo ideal es que esto se gestionara de forma centralizada
                // Se puede ver c??mo hacerlo en la formaci??n sobre Validaci??n con Spring Boot
                // y la formaci??n sobre Gesti??n de Errores con Spring Boot
                throw new RuntimeException();
            }
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password Data Error");
        }

        return null;
    }

    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal User loggedUser) {
        return UserResponse.fromUser(loggedUser);
    }

    @PutMapping("/admin/disable/{id}")
    public UserResponse disableAccount(@PathVariable String id) {
        return UserResponse.fromUser(userService.disable(UUID.fromString(id)));
    }

    @PutMapping("/admin/enable/{id}")
    public UserResponse enableAccount(@PathVariable String id) {
        return UserResponse.fromUser(userService.enable(UUID.fromString(id)));
    }

    @GetMapping("/bookmarks")
    public List<BookResponse> getBookmarks(@AuthenticationPrincipal User loggedUser) {
        return userService.findBookmarks(loggedUser.getId()).stream().map(BookResponse::of).toList();
    }
}
