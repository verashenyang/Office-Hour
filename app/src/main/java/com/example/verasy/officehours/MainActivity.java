package com.example.verasy.officehours;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

//import android.support.v7.app.AppCompatActivity;

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

        ListView listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.courses_list, courselist);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(courselist[position]=="CS591"){
                    Intent i = new Intent(MainActivity.this,CourseActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}