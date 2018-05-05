package com.ar.tdp2fiuba.hoycomo.model;

import com.google.gson.Gson;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String avatar;

    public User(String userId, String firstName, String lastName, String avatar) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatar() {
        return avatar;
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
