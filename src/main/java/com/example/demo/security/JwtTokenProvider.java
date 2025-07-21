//package com.example.demo.security;
//
//import com.example.demo.entites.User;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtTokenProvider {
//
//    private final Key key;
//
//    @Value("${app.jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${app.jwt.expiration-in-ms}")
//    private int jwtExpirationInMs;
//
//    public JwtTokenProvider() {
//        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//    }
//
//    public String generateToken(Authentication authentication) {
//        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//        return generateTokenFromUserId(userPrincipal.getId());
//    }
//
//    public String generateToken(User user) {
//        return generateTokenFromUserId(user.getId());
//    }
//
//    private String generateTokenFromUserId(Long userId) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
//
//        return Jwts.builder()
//                .setSubject(Long.toString(userId))
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    public Long getUserIdFromToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return Long.parseLong(claims.getSubject());
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(authToken);
//            return true;
//        } catch (JwtException ex) {
//            // Log exception if needed
//        }
//        return false;
//    }
//}