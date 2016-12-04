package com.example.verasy.officehours;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity implements SearchListener {

    HashMap<String, Object> databaseEntries = new HashMap<>();
    ArrayList<String> listData = new ArrayList<>();

    ArrayAdapter adapter;

    final String classesKey = "classes";
    final String professorsKey = "professors";
    String currentType = classesKey;

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
        //SearchFragment searchFrag = new SearchFragment();
        //searchFrag.setArguments(bundle);

        // have to cast to string because these are returned as generic objects
        final String term = (String)b.get("term");
        final String type = (String)b.get("type");
        final Long userId = (Long)b.get("user");

        Log.e("debug", "Search Activity" + type);

        // Get data from Firebase Database
        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)listView.getItemAtPosition(position);
                HashMap<String, Object> currentListing = (HashMap<String, Object>) databaseEntries.get(currentType);

                /* If current type is classes, start new courses actvitiy
                 * Otherwise if current type is professors, start new professors activity */
                if (currentType.equals(classesKey)) {
                    Intent classesIntent = new Intent(SearchActivity.this, CoursesActivity.class);
                    classesIntent.putExtra("name", selection);
                    classesIntent.putExtra("content", (HashMap<String, HashMap<String, String>>) currentListing.get(selection));
                    classesIntent.putExtra("user", userId);
                    startActivity(classesIntent);
                } else if (currentType.equals(professorsKey)) {
                    Intent professorIntent = new Intent(SearchActivity.this, ProfessorActivity.class);
                    professorIntent.putExtra("name", selection);
                    professorIntent.putExtra("content", (HashMap<String, Object>) currentListing.get(selection));
                    professorIntent.putExtra("user", userId);
                    startActivity(professorIntent);
                }
            }
        });
    }

    private void getData() {
        // Get reference to Firebase Real Time Database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get reference to dictionary matching data types
        DatabaseReference classesReference = databaseReference.child(classesKey);
        DatabaseReference professorsReference = databaseReference.child(professorsKey);

        // Add single value event listener for classes type
        ValueEventListener classesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store classes in classes dictionary
                databaseEntries.put(classesKey, (HashMap<String, Object>) dataSnapshot.getValue());

                // Add all results to searchResults array
                listData.addAll(((HashMap<String, Object>) databaseEntries.get(classesKey)).keySet());

                // Update ListView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ExceptionTag", "Cannot get classes from firebase");
            }
        };
        classesReference.addValueEventListener(classesListener);

        // Add single value event listener for professor type
        ValueEventListener professorsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store professors in professors dictionary
                databaseEntries.put(professorsKey, (HashMap<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ExceptionTag", "Cannot get professors from firebase");
            }
        };
        professorsReference.addValueEventListener(professorsListener);
    }

    // MARK: Search Fragment Interface

    @Override
    public void searchClass(String searchString) {
        // Get list of keys from databaseEntries["classes"]
        ArrayList<String> keys = new ArrayList<>(((HashMap<String, Object>)databaseEntries.get(classesKey)).keySet());

        // Clear data to display in ListView
        listData.clear();

        // Find search matches from keys
        for (String className : keys) {
            if (className.toLowerCase().contains(searchString.toLowerCase())) {
                listData.add(className);
            }
        }

        currentType = classesKey;

        // Update ListView
        adapter.notifyDataSetChanged();
    }

    @Override
    public void searchProf(String searchString) {
        // Get list of keys from databaseEntries["professors"]
        ArrayList<String> keys = new ArrayList<>(((HashMap<String, Object>)databaseEntries.get(professorsKey)).keySet());

        // Clear data to display in ListView
        listData.clear();

        // Find search matches from keys
        for (String professorName : keys) {
            if (professorName.toLowerCase().contains(searchString.toLowerCase())) {
                listData.add(professorName);
            }
        }

        currentType = professorsKey;

        // Update ListView
        adapter.notifyDataSetChanged();
    }
}