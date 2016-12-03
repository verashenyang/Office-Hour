package com.example.verasy.officehours;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;



public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "h9sQY3kRKfXZHMkk4jjwKjAsR";
    private static final String TWITTER_SECRET = "gTNRcSzmg1jDngvhvEpn6g7EbMwLyW8ZNejWOcC1xcmY0909PF";
    private Intent i;//get intent from SearchActivity to see which fragment we should show
    private String type;
    String TAG = "Test";
    private FrameLayout prof;
    private FrameLayout courses;
    private FragmentManager fm;
    //private ProfFragment prof_frag;
    //private CoursesFragment courses_frag;
    private long userId;
    String[] courselist = {"CS591", "CS112"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        Bundle i = getIntent().getExtras();

        // retrieve the data type received from search, and the user id, defaulting to 0 if there is none
        type = i.getString("type");
        userId = i.getLong("user", 0);
        fm = getFragmentManager();
        prof = (FrameLayout) findViewById(R.id.prof);
        courses = (FrameLayout) findViewById(R.id.courses);

        Log.e("test", type + " is type");
        Log.e("test", userId + " is userId");

        //temporarily show prof_fragment first
        prof = (FrameLayout) findViewById(R.id.prof);
        courses = (FrameLayout) findViewById(R.id.courses);
        courses.setVisibility(View.GONE);
        prof.setVisibility(View.VISIBLE);
        initialProf();//initial proffragment
    }

    @Override
    protected void onStart() {
        super.onStart();

        //initial fragment //TO DO:get data from database and set data on the page
        //prof_frag = (ProfFragment) fm.findFragmentById(prof_fragment);
        //courses_frag = (CoursesFragment) fm.findFragmentById(courses_fragment);

        //judge which fragment we should show
        if (type.equals("prof")) {
            prof.setVisibility(View.VISIBLE);
            courses.setVisibility(View.INVISIBLE);
            initialProf(); //initial proffragment
            //Log.i(TAG, "this is prof");
        } else if (type.equals("classes")) {
            prof.setVisibility(View.INVISIBLE);
            courses.setVisibility(View.VISIBLE);
            //Log.i(TAG, "this is classes");
        }
    }

    protected void initialProf() {
        /*ListView listView = (ListView) findViewById(R.id.menu);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.courses_list, courselist);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (courselist[position] == "CS591") {
                    FrameLayout courses = (FrameLayout) findViewById(R.id.courses);
                    courses.setVisibility(View.VISIBLE);
                    FrameLayout prof = (FrameLayout) findViewById(R.id.prof);
                    prof.setVisibility(View.GONE);

                }
            }
        });

        Button review = (Button) findViewById(R.id.review);
        review.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReviewRateActivity.class);
                startActivity(intent);
            }
        });*/
    }

}