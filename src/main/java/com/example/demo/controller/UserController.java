package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entites.User;
import com.example.demo.exception.AuthenticationException;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
//import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    // private final AuthService authService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole("USER");
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.getUserByUsername(loginRequest.getUsername());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<Optional<User>> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(Optional.ofNullable(userService.getUserByEmail(email)));
    }

    // @GetMapping("/me")
    // public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
    //     User user = authService.getUserFromToken(token.replace("Bearer ", ""));
    //     return ResponseEntity.ok(user);
    // }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthException(AuthenticationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}