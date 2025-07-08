package com.user_login.User.Login.service;

import com.user_login.User.Login.io.ProfileRequest;
import com.user_login.User.Login.io.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface ProfileService {

    ProfileResponse createProfile(ProfileRequest request);

    ProfileResponse getProfile(String email);
}
