package com.user_login.User.Login.controller;

import com.user_login.User.Login.io.ProfileRequest;
import com.user_login.User.Login.io.ProfileResponse;
import com.user_login.User.Login.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse registerUser(@Valid @RequestBody ProfileRequest profileRequest) {
        return profileService.createProfile(profileRequest);
    }

    /*@GetMapping("/test")
    public String test(){
        return "auth working fine";
    }*/

    @GetMapping ("/profile")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return profileService.getProfile(email);
    }
}
