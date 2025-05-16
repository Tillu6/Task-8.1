package com.example.chatbot.API;

import com.google.gson.annotations.SerializedName;

public class ResponsePost {
    @SerializedName("username")
    private String username;

    @SerializedName("message")
    private String message;

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
