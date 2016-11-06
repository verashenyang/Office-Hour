package com.example.verasy.officehours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    //TODO: this needs to be removed when we populate data via SQL searches
    String [] dummyData = {"Homer", "Shereif", "Fake Class", "Blah Blah", "other stuff", "Nonsense", "cuckoo"};
    HashMap<String, Object> classes = new HashMap<String, Object>();
    ArrayList<String> classesNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.search_result, classesNames);

        ListView listView = (ListView) findViewById(R.id.searchResults);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO we need to retrieve data stored in the item here and pass it to the intent
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Get reference to database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get reference to classes dictionary
        DatabaseReference coursesReference = databaseReference.child("classes");

        // Add single value event listener for classes
        ValueEventListener coursesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Store classes in classes dictionary
                classes  = (HashMap<String, Object>) dataSnapshot.getValue();

                // Get all keys and add to classesName array
                classesNames.addAll(classes.keySet());

                // Update listview
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        coursesReference.addListenerForSingleValueEvent(coursesListener);
    }
}
