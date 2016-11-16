package com.example.verasy.officehours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CoursesActivity extends AppCompatActivity {

    TextView courseTitleTextView, courseOfficeHoursTextView, courseLocationTextView, courseProfessorTextView, courseDescriptionTextView;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        courseTitleTextView = (TextView) findViewById(R.id.courseTitleTextView);
        courseOfficeHoursTextView = (TextView) findViewById(R.id.courseOfficeHoursTextView);
        courseLocationTextView = (TextView) findViewById(R.id.courseLocationTextView);
        courseProfessorTextView = (TextView) findViewById(R.id.courseProfessorTextView);
        courseDescriptionTextView =(TextView) findViewById(R.id.courseDescriptionTextView);
        btnSave = (Button)findViewById(R.id.saveCourse);

        // Get intent that created current Activity
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        String courseName = (String) intentBundle.get("name");
        HashMap<String, String> courseContent = (HashMap<String, String>) intentBundle.get("content");
        final long userId = (Long)intentBundle.get("user");
        final String courseId = courseContent.get("courseTitle");
        final String courseDescription = courseContent.get("courseDescription");

        setTitle(courseName);
        updateLabelsForCourse(courseContent);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo add the course id to the list of courses under the current student id
                //if the student id is 0, skip
                if(userId==0) {
                    return;
                }
                // Get reference to database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                // Get reference to specific users class list in the db
                DatabaseReference classRef = databaseReference.child("users")
                        .child(Long.toString(userId))
                        .child("classes");

                // sets the course name and description
                classRef.child(courseId).setValue(courseDescription);
            }
        });
    }

    private void updateLabelsForCourse(HashMap<String, String> courseContent) {
        String courseTitle = "N/A";
        if (courseContent.containsKey("courseTitle")) {
            courseTitle = courseContent.get("courseTitle");
        }

        String courseProfessor = "N/A";
        if (courseContent.containsKey("professor")) {
            courseProfessor = courseContent.get("professor");
        }

        String courseLocation = "N/A";
        if (courseContent.containsKey("courseLocation")) {
            courseLocation = courseContent.get("courseLocation");
        }

        String courseOfficeHours = "N/A";
        if (courseContent.containsKey("officeHours")) {
            courseOfficeHours = courseContent.get("officeHours");
        }

        String courseDescription = "N/A";
        if (courseContent.containsKey("courseDescription")) {
            courseDescription = courseContent.get("courseDescription");
        }

        String courseLectureTimes = "N/A";
        if (courseContent.containsKey("lectureTimes")) {
            courseLectureTimes = courseContent.get("lectureTimes");
        }

        courseTitleTextView.setText(courseTitle);
        courseProfessorTextView.setText(courseProfessor);
        courseLocationTextView.setText(courseLocation);
        courseOfficeHoursTextView.setText(courseOfficeHours);
        courseDescriptionTextView.setText(courseDescription);
    }
}
