package com.example.verasy.officehours;

import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ProfessorActivity extends AppCompatActivity {

    TextView officeTextView, emailTextView, classesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        officeTextView = (TextView) findViewById(R.id.officeTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        classesTextView = (TextView) findViewById(R.id.classesTextView);

        // Get intent that created current Activity
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        String professorName = (String) intentBundle.get("name");
        HashMap<String, String> professorData = (HashMap<String, String>) intentBundle.get("content");

        /* IF USING TIMESTAMPS IN PROFESSORS, USE CODE BELOW
         * HashMap<String, String> professorData;
         * ArrayList<String> timestamps = (ArrayList<String>) content.keySet();
         * Collections.sort(timestamps);
         * professorData = (HashMap<String, String>) content.get(timestamps.get(timestamps.size() - 1));
         */

        setTitle(professorName);
        updateLabelsForProfessor(professorData);
    }

    private void updateLabelsForProfessor(HashMap<String, String> professorData) {
        String office = "N/A";
        if (professorData.containsKey("office")) {
            office = professorData.get("office");
        }

        String email = "N/A";
        if (professorData.containsKey("email")) {
            email = professorData.get("email");
        }

        String classes = "N/A";
        if (professorData.containsKey("classes")) {
            classes = professorData.get("classes");
        }

        officeTextView.setText(office);
        emailTextView.setText(email);
        classesTextView.setText(classes);
    }
}
