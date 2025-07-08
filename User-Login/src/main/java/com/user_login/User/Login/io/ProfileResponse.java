package com.user_login.User.Login.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {

    private String username;
    private String name;
    private String email;
    private boolean isAccountVerified;
}
