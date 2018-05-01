package com.ar.tdp2fiuba.hoycomo.model;

import com.google.gson.Gson;

public class User {
    private String userId;
    private String token;
    private String firstName;
    private String lastName;

    public User(String userId, String token, String firstName, String lastName) {
        this.userId = userId;
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return userId != null ? userId.equals(user.userId) : user.userId == null;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
