package com.example.verasy.officehours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    //TODO: this needs to be removed when we populate data via SQL searches
    String [] dummyData = {"Homer", "Shereif", "Fake Class", "Blah Blah", "other stuff", "Nonsense", "cuckoo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.search_result, dummyData);

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
    }
}
