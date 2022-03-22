package com.example.xchange;

public class User {

    public String username, location, email;
    public String phoneNumber;

    public User() {

    }

    public User(String username, String location, String email, String phoneNumber) {
        this.username = username;
        this.location = location;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
