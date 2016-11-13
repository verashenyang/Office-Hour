package com.example.verasy.officehours;

/**
 * Created by verasy on 11/8/16.
 */

public class ReviewObject {
    public String prof_name;
    public String comment;
    public Float rating;

    public ReviewObject(String prof_name, String comment, Float rating) {
        this.prof_name = prof_name;
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public Float getRating() {
        return rating;
    }
}
