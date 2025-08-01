package com.user_login.User.Login.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public String generateToken(UserDetails userDetails){

        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userDetails.getUsername());

    }

    private String createToken(Map<String, Object> claims, String email ) {
        return Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private Claims parseToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimByToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameByToken(String token){
        return getClaimByToken(token,Claims::getSubject);
    }

    public Date getExpirationDateByToken(String token){
        return getClaimByToken(token,Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpirationDateByToken(token).before(new Date());
    }

    public boolean validateToken(String token,UserDetails userDetails){
        final String email=getUsernameByToken(token);
        return (!isTokenExpired(token) && email.equals(userDetails.getUsername() ));
    }
}
