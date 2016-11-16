package com.example.verasy.officehours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements SearchListener {

    HashMap<String, Object> databaseEntries = new HashMap<>();
    ArrayList<String> listData = new ArrayList<>();

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // ArrayAdapter to populate Search_Result ListView
        adapter = new ArrayAdapter<>(this, R.layout.search_result, listData);

        // ListView to display search results
        final ListView listView = (ListView) findViewById(R.id.searchResults);
        listView.setAdapter(adapter);

        // Get intent that created current SearchActivity
        final Intent intent = getIntent();
        Bundle b = intent.getExtras();

        long user = b.getLong("user");
        Log.e("test", "Search Activity " + user);

        //make sure to send the user id to the search fragment so it can pass it on to other activities
        // Create SearchFragmnet
        Bundle bundle = new Bundle();
        bundle.putLong("user", b.getLong("user"));
        SearchFragment searchFrag = new SearchFragment();
        searchFrag.setArguments(bundle);

        // have to cast to string because these are returned as generic objects
        final String term = (String)b.get("term");
        final String type = (String)b.get("type");
        final Long userId = (Long)b.get("user");

        // Get data from Firebase Database
        getData(type);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)listView.getItemAtPosition(position);

                // Start new classes activity if type == classes, otherwise do like before
                if (type.equals("classes")) {
                    Intent coursesIntent = new Intent(SearchActivity.this, CoursesActivity.class);
                    coursesIntent.putExtra("name", selection);
                    coursesIntent.putExtra("content", (HashMap<String, String>) databaseEntries.get(selection));
                    coursesIntent.putExtra("user", userId);
                    startActivity(coursesIntent);
                } else if (type.equals("prof")) {
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("name", selection);
                    intent.putExtra("content", (HashMap<String, Object>) databaseEntries.get(selection));
                    intent.putExtra("user", userId);
                    startActivity(intent);
                }
            }
        });
    }

    private void getData(String dataType) {
        // Get reference to Firebase Real Time Database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get reference to dictionary matching data type
        DatabaseReference coursesReference = databaseReference.child(dataType);

        // Add single value event listener for data type
        ValueEventListener coursesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store classes in classes dictionary
                databaseEntries  = (HashMap<String, Object>) dataSnapshot.getValue();

                // Add all results to searchResults array
                listData.clear();
                listData.addAll(databaseEntries.keySet());

                // Update ListView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        coursesReference.addListenerForSingleValueEvent(coursesListener);
    }

    @Override
    public void search(String string) {
        // Get list of keys from databaseEntries
        ArrayList<String> keys = new ArrayList<>(databaseEntries.keySet());

        // Clear data to display in ListView
        listData.clear();

        // Find search matches from keys
        for (String className : keys) {
            if (className.toLowerCase().contains(string.toLowerCase())) {
                listData.add(className);
            }
        }

        // Update ListView
        adapter.notifyDataSetChanged();
    }
}
