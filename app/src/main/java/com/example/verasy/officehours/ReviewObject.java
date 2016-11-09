package com.example.verasy.officehours;

/**
 * Created by verasy on 11/8/16.
 */

public class ReviewObject {
    private String comment;
    private Float rating;

    public ReviewObject(String a, Float b) {
        this.comment = a;
        this.rating = b;
    }

    public String getComment() {
        return comment;
    }

    public Float getRating() {
        return rating;
    }
}
