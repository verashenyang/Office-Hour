package com.example.verasy.officehours;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    String[] courselist = {"CS591","CS112"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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