package com.example.chatbot.API;

import com.example.chatbot.API.models.AuthRequest;
import com.example.chatbot.API.models.VerifyUsernameResponse;
import com.example.chatbot.API.models.ChatRequest;
import com.example.chatbot.API.models.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {
    // POST /verify-username → { "username": "saketh" }
    @Headers("Content-Type: application/json")
    @POST("verify-username")
    Call<VerifyUsernameResponse> initUser(@Body AuthRequest authRequest);

    // POST /chat → { "message": "..." }
    @Headers("Content-Type: application/json")
    @POST("chat")
    Call<ChatResponse> getChatResponse(@Body ChatRequest chatRequest);
}
