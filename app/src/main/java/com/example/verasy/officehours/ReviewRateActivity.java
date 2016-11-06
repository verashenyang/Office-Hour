package com.example.verasy.officehours;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ReviewRateActivity extends AppCompatActivity {
    String[] reviewslist = {"Good","Excellent"};
    private static final String TAG="review";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"review");
        setContentView(R.layout.activity_review_rate);

        ListView listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.reviews_list, reviewslist);
        listView.setAdapter(adapter);
    }
}
