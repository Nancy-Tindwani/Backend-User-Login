package com.user_login.User.Login.controller;

import com.user_login.User.Login.io.AuthRequest;
import com.user_login.User.Login.io.AuthResponse;
import com.user_login.User.Login.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try{
            authenticate(authRequest.getEmail(),authRequest.getPassword());
            final UserDetails userDetails =userDetailsService.loadUserByUsername(authRequest.getEmail());
            final String jwtToken=jwtUtil.generateToken(userDetails);
            ResponseCookie cookie=ResponseCookie.from("jwt",jwtToken).httpOnly(true).path("/").
                    maxAge(Duration.ofDays(1)).sameSite("Strict").build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).
                    body(new AuthResponse(authRequest.getEmail(),jwtToken));
        }
        catch (BadCredentialsException ex){
            Map<String,String> error = new HashMap<>();
            error.put("error", String.valueOf(true));
            error.put("message","Bad Email or Password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toString());
        }
        catch (DisabledException ex){
            Map<String,String> error = new HashMap<>();
            error.put("error", String.valueOf(true));
            error.put("message","Account Disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error.toString());
        }
        catch (Exception ex){
            Map<String,String> error = new HashMap<>();
            error.put("error", String.valueOf(true));
            error.put("message","Authentication Failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.toString());
        }
        //return new ResponseEntity<>(HttpStatus.OK);
    }

    private void authenticate(String email, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
