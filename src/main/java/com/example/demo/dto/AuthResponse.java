package com.example.demo.dto;

import com.example.demo.entites.User;

public class AuthResponse {
    private String token;
    private User user;

    public AuthResponse() {}
    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}