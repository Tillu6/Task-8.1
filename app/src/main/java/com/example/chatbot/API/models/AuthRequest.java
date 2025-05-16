package com.example.chatbot.API.models;

public class AuthRequest {
    private String username;
    public AuthRequest(String username) { this.username = username; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
