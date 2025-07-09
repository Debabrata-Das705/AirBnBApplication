package com.debabrata.AirBnbApllication.security;


import com.debabrata.AirBnbApllication.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    @Value(("${jwt.secretkey}"))
    private String jwtSecretKey;

    private SecretKey getSecretkeay(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("roles",user.getRoles().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .signWith(getSecretkeay())
                .compact();
    }
    public String generateRefreshToken(User user){
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(getSecretkeay())
                .compact();
    }

     public Long getUserIdFromToken(String token){
         Claims claims= Jwts.parser()
                 .verifyWith(getSecretkeay())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload();
         return Long.valueOf(claims.getSubject());
     }
}
