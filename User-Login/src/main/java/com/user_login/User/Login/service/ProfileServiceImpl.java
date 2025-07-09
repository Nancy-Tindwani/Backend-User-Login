package com.user_login.User.Login.service;

import com.user_login.User.Login.entity.UserEntity;
import com.user_login.User.Login.io.ProfileRequest;
import com.user_login.User.Login.io.ProfileResponse;
import com.user_login.User.Login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class ProfileServiceImpl implements ProfileService {

private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;

@Override
    public ProfileResponse createProfile(ProfileRequest request)
{
    UserEntity userEntity=convertToUserEntity(request);
    if(!userRepository.existsByEmail(request.getEmail())){
        userEntity=userRepository.save(userEntity);
        return convertToProfileResponse(userEntity);
    }
     throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");

}

@Override
public ProfileResponse getProfile(String email)
{
    UserEntity existingUser=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
    return convertToProfileResponse(existingUser);
}

    @Override
    public void resetPassword(String email,String newPassword) {
        UserEntity existingUser=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        if (newPassword!=null) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }
        userRepository.save(existingUser);
    }

    @Override
    public String getLoggedInUserName(String email) {
    UserEntity existingUser=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return existingUser.getUsername();
    }


    private ProfileResponse convertToProfileResponse(UserEntity userEntity) {

    return ProfileResponse.builder().
            name(userEntity.getName()).
            username(userEntity.getUsername()).
            email(userEntity.getEmail()).
            isAccountVerified(userEntity.isAccountVerified()).build();
    }

    private UserEntity convertToUserEntity(ProfileRequest request) {

       return UserEntity.builder()
                .email(request.getEmail())
                .username(UUID.randomUUID().toString())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .build();
    }

}
