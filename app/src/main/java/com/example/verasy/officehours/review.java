package com.example.verasy.officehours;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jianzhou on 11/1/16.
 */

@IgnoreExtraProperties
public class review {

    public String prof_name;
    public float rating;
    public String comment;

    public review(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public review(String prof_name, float rating, String comment ){
        this.prof_name = prof_name;
        this.rating = rating;
        this.comment = comment;
    }

}
