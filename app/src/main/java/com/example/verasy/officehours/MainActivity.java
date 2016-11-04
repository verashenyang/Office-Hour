package com.example.verasy.officehours;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "h9sQY3kRKfXZHMkk4jjwKjAsR";
    private static final String TWITTER_SECRET = "gTNRcSzmg1jDngvhvEpn6g7EbMwLyW8ZNejWOcC1xcmY0909PF";

    String[] courselist = {"CS591","CS112"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        FrameLayout prof = (FrameLayout)findViewById(R.id.prof);
        prof.setVisibility(View.VISIBLE);
        FrameLayout courses = (FrameLayout)findViewById(R.id.courses);
        courses.setVisibility(View.INVISIBLE);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.courses_list, courselist);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        // Get reference to the Firebase Real Time Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // Add a event listener for changes to the database
        ValueEventListener testListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value for key "test"
                String value = (String) dataSnapshot.child("test").getValue();

                // Log with tag "testTag"
                Log.e("testTag", value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        database.addValueEventListener(testListener);
    }
}