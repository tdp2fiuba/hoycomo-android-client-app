package com.ar.tdp2fiuba.hoycomo.model;

public class Review {
    private Double rating;
    private String storeId;
    private User user;
    private String body;
    private String timestamp;

    public Review(Double rating, String body) {
        this.rating = rating;
        this.body = body;
    }

    public Review(Double rating, String storeId, User user, String body, String timestamp) {
        this.rating = rating;
        this.storeId = storeId;
        this.user = user;
        this.body = body;
        this.timestamp = timestamp;
    }

    public Double getRating() {
        return rating;
    }

    public String getStoreId() {
        return storeId;
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

        Review review = (Review) o;

        if (rating != null ? !rating.equals(review.rating) : review.rating != null) return false;
        if (storeId != null ? !storeId.equals(review.storeId) : review.storeId != null)
            return false;
        if (user != null ? !user.equals(review.user) : review.user != null) return false;
        if (body != null ? !body.equals(review.body) : review.body != null) return false;
        return timestamp != null ? timestamp.equals(review.timestamp) : review.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = rating != null ? rating.hashCode() : 0;
        result = 31 * result + (storeId != null ? storeId.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                ", storeId='" + storeId + '\'' +
                ", user=" + user +
                ", body='" + body + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
