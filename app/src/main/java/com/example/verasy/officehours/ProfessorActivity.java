package com.example.verasy.officehours;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class ProfessorActivity extends AppCompatActivity {

    TextView officeTextView, emailTextView, classesTextView;
    Button btnReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        officeTextView = (TextView) findViewById(R.id.officeTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        classesTextView = (TextView) findViewById(R.id.classesTextView);
        btnReview = (Button) findViewById(R.id.review);


        // Get intent that created current Activity
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        final String professorName = (String) intentBundle.get("name");
        HashMap<String, String> professorData = (HashMap<String, String>) intentBundle.get("content");

        /* IF USING TIMESTAMPS IN PROFESSORS, USE CODE BELOW
         * HashMap<String, String> professorData;
         * ArrayList<String> timestamps = (ArrayList<String>) content.keySet();
         * Collections.sort(timestamps);
         * professorData = (HashMap<String, String>) content.get(timestamps.get(timestamps.size() - 1));
         */

        setTitle(professorName);
        updateLabelsForProfessor(professorData);


        // create an intent with "profName" and start ReviewRateActivity by clicking Button btnReview
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewPage = new Intent(ProfessorActivity.this, ReviewRateActivity.class);
                reviewPage.putExtra("ProfName", professorName);
                startActivity(reviewPage);
            }
        });
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
