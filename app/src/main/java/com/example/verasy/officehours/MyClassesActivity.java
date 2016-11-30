package com.example.verasy.officehours;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MyClassesActivity extends AppCompatActivity {

    HashMap<String, Object> databaseEntries = new HashMap<>();
    ArrayList<String> listData = new ArrayList<>();

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes);

        // ArrayAdapter to populate the ListView
        adapter = new ArrayAdapter<>(this, R.layout.search_result, listData);

        // ListView to display classes
        final ListView listView = (ListView) findViewById(R.id.myClasses);
        listView.setAdapter(adapter);

        // Get intent that created current Activity
        final Intent intent = getIntent();
        Bundle b = intent.getExtras();

        // have to cast to string because these are returned as generic objects
        final Long userId = (Long)b.get("user");

        // give the user a way out in case they dont want to view their classes
        Button btnGoToSearch = (Button)findViewById(R.id.btnGoToSearch);
        btnGoToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyClassesActivity.this, SearchActivity.class);
                intent.putExtra("user", userId);
                intent.putExtra("type", "classes");
                startActivity(intent);
            }
        });

        // Get class data for the user from Firebase Database
        getClasses(Long.toString(userId));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // extracts the class id from the string contained in the list view
                String selection = ((String)listView.getItemAtPosition(position)).split("\t")[0];

                // Start new classes activity when a user's class is selected
                Intent coursesIntent = new Intent(MyClassesActivity.this, CoursesActivity.class);
                coursesIntent.putExtra("name", selection);
                coursesIntent.putExtra("content", (HashMap<String, String>) databaseEntries.get(selection));
                coursesIntent.putExtra("user", userId);
                startActivity(coursesIntent);
            }
        });
    }

    private void getClasses(String userId) {
        // Get reference to Firebase Real Time Database
        Log.e("test", userId);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get reference to the specific user's classes
        DatabaseReference coursesReference = databaseReference.child("users").child(userId).child("classes");
//        DatabaseReference coursesReference = databaseReference.child("user");


        // Add single value event listener for data type
        ValueEventListener coursesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store classes in classes dictionary
                databaseEntries  = (HashMap<String, Object>) dataSnapshot.getValue();

                // Add all results to searchResults array
                Set<String> keySet = databaseEntries.keySet();
                listData.clear();

                for(String item : keySet) {
                    listData.add(item + "\t\t" + (String)databaseEntries.get(item));
                }

                // Update ListView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        coursesReference.addListenerForSingleValueEvent(coursesListener);
    }
}
