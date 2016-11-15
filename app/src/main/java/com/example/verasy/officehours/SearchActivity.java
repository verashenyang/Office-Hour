package com.example.verasy.officehours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
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

public class SearchActivity extends AppCompatActivity {

    HashMap<String, Object> classes = new HashMap<String, Object>();
    ArrayList<String> classesNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.search_result, classesNames);

        // retrieve the intent received from the search fragment
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        long user = b.getLong("user");
        Log.e("test", "Search Activity " + user);

        //make sure to send the user id to the search fragment so it can pass it on to other activities
        Bundle bundle = new Bundle();
        bundle.putLong("user", b.getLong("user"));
        SearchFragment searchFrag = new SearchFragment();
        searchFrag.setArguments(bundle);

        // have to cast to string because these are returned as generic objects
        final String term = (String)b.get("term");
        final String type = (String)b.get("type");
        final Long userId = (Long)b.get("user");

        final ListView listView = (ListView) findViewById(R.id.searchResults);
        listView.setAdapter(adapter);

        // Get reference to database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get reference to table dictionary depending on arguments received
        DatabaseReference coursesReference = databaseReference.child(type);

        // Add single value event listener for classes
        ValueEventListener coursesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Store classes in classes dictionary
                classes  = (HashMap<String, Object>) dataSnapshot.getValue();

                // Get all keys and add to classesName array
                Log.e("test", "This ran with term: " + term);
                for(String key: classes.keySet()) {
                    if(key.contains(term)) {
                        classesNames.add(key);
                    }
                }

                // Update listview
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        coursesReference.addListenerForSingleValueEvent(coursesListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String)listView.getItemAtPosition(position);
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("name", selection);
                intent.putExtra("content", (HashMap<String, Object>)classes.get(selection));
                intent.putExtra("user", userId);
                startActivity(intent);
            }
        });
    }
}
