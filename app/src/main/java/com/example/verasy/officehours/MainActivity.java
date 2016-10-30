package com.example.verasy.officehours;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.verasy.officehours.R.id.courses;
import static com.example.verasy.officehours.R.id.prof;

//import android.support.v7.app.AppCompatActivity;

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

    }
}