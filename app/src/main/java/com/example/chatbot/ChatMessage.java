package com.example.chatbot;

public class ChatMessage {
    private String message;
    public enum AUTHOR_TYPE {
        AUTHOR_TYPE_USER,
        AUTHOR_TYPE_AI
    }
    private AUTHOR_TYPE author;

    public ChatMessage(String message, AUTHOR_TYPE author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AUTHOR_TYPE getAuthor() {
        return author;
    }

    public void setAuthor(AUTHOR_TYPE author) {
        this.author = author;
    }
}
