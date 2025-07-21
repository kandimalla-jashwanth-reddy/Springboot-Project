//package com.example.demo.service;
//
//import com.example.demo.dto.AuthResponse;
//import com.example.demo.entites.User;
//import com.example.demo.exception.AuthenticationException;
//import com.example.demo.security.JwtTokenProvider;
//import com.example.demo.security.UserPrincipal;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final AuthenticationManager authenticationManager;
//
//    public AuthService(UserService userService, PasswordEncoder passwordEncoder,
//                       JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
//        this.userService = userService;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.authenticationManager = authenticationManager;
//    }
//
//    public AuthResponse registerUser(String username, String email, String password) {
//        if (userService.existsByUsername(username)) {
//            throw new AuthenticationException("Username is already taken");
//        }
//
//        if (userService.existsByEmail(email)) {
//            throw new AuthenticationException("Email is already in use");
//        }
//
//        User user = new User();
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole("ROLE_USER");
//
//        User savedUser = userService.saveUser(user);
//        String token = jwtTokenProvider.generateToken(savedUser);
//
//        return new AuthResponse(token, savedUser);
//    }
//
//    public AuthResponse authenticateUser(String username, String password) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//
//            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//            User user = userService.getUserById(userPrincipal.getId());
//            String token = jwtTokenProvider.generateToken(authentication);
//
//            return new AuthResponse(token, user);
//        } catch (org.springframework.security.core.AuthenticationException e) {
//            throw new AuthenticationException("Invalid username/password");
//        }
//    }
//
//    public User getUserFromToken(String token) {
//        Long userId = jwtTokenProvider.getUserIdFromToken(token);
//        return userService.getUserById(userId);
//    }
//}