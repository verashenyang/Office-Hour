package com.example.verasy.officehours;

import android.content.Intent;
import android.graphics.PorterDuff;
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

    HashMap<String, Object> databaseEntries, classEntry = new HashMap<>();
    ArrayList<String> listData = new ArrayList<>();

    final String classesKey = "classes";
    private Long userId;

    ArrayAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        getMyClasses(Long.toString(userId));
    }

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
        userId = (Long)b.get("user");

        // give the user a way out in case they dont want to view their classes
        Button btnGoToSearch = (Button)findViewById(R.id.btnGoToSearch);
        btnGoToSearch.getBackground().setColorFilter(0xFF5baaf4, PorterDuff.Mode.MULTIPLY);
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
        getMyClasses(Long.toString(userId));
        // get the full class library to send to the next activity
        getAllClasses();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // extracts the class id from the string contained in the list view
                Log.e("test", classEntry.keySet().toArray()[0].toString());
                HashMap<String, Object> currentListing = (HashMap<String, Object>) classEntry.get(classesKey);
                Log.e("test", currentListing.keySet().toArray()[0].toString());
                String selection = ((String)listView.getItemAtPosition(position)).split("\t")[0];

                // Start new classes activity when a user's class is selected
                Intent coursesIntent = new Intent(MyClassesActivity.this, CoursesActivity.class);
                coursesIntent.putExtra("name", selection);
                coursesIntent.putExtra("content", (HashMap<String, HashMap<String, String>>)currentListing.get(selection));
                coursesIntent.putExtra("user", userId);
                startActivity(coursesIntent);
            }
        });
    }

    private void getAllClasses(){
        // Get reference to Firebase Real Time Database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get reference to dictionary matching data types
        DatabaseReference classesReference = databaseReference.child(classesKey);

        // Add single value event listener for classes type
        ValueEventListener classesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store classes in classes dictionary
                classEntry.put(classesKey, dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ExceptionTag", "Cannot get classes from firebase");
            }
        };
        classesReference.addListenerForSingleValueEvent(classesListener);

    }

    private void getMyClasses(String userId) {
        // Get reference to Firebase Real Time Database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get reference to the specific user's classes
        DatabaseReference coursesReference = databaseReference.child("users").child(userId).child("classes");

        // Add single value event listener for data type
        ValueEventListener coursesListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store classes in classes dictionary
                databaseEntries  = (HashMap<String, Object>) dataSnapshot.getValue();

                if (databaseEntries != null) {
                    // Add all results to searchResults array
                    Set<String> keySet = databaseEntries.keySet();
                    listData.clear();

                    for (String item : keySet) {
                        listData.add(item + "\t\t" + (String) databaseEntries.get(item));
                    }
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
