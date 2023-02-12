package com.capibaraanonimo.myanonamousepdf.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    private String username;
    private String password;
    private String verifyPassword;
    private String avatar;
    private String fullName;

}
