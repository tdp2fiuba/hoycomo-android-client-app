package com.ar.tdp2fiuba.hoycomo.model;

public class Review {
    private Double rating;
    private User user;
    private String body;
    private String timestamp;

    public Review(Double rating, String body) {
        this.rating = rating;
        this.body = body;
    }

    public Review(Double rating, User user, String body, String timestamp) {
        this.rating = rating;
        this.user = user;
        this.body = body;
        this.timestamp = timestamp;
    }

    public Double getRating() {
        return rating;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review1 = (Review) o;

        if (rating != null ? !rating.equals(review1.rating) : review1.rating != null) return false;
        if (user != null ? !user.equals(review1.user) : review1.user != null) return false;
        if (body != null ? !body.equals(review1.body) : review1.body != null) return false;
        return timestamp != null ? timestamp.equals(review1.timestamp) : review1.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = rating != null ? rating.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                ", user=" + user +
                ", body='" + body + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
