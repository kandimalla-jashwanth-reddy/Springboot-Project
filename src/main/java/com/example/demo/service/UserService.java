package com.example.demo.service;

import com.example.demo.entites.User;
import java.util.List;

public interface UserService {
    User saveUser(User user);
    User getUserById(Long id);
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> getAllUsers();
    void deleteUser(Long id);
}