package com.example.verasy.officehours;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.models.HashtagEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CoursesActivity extends AppCompatActivity {

    TextView courseTitleTextView, courseOfficeHoursTextView, courseLocationTextView, courseProfessorTextView, courseDescriptionTextView;
    Button edit, update, btnSave;
    LinearLayout editLayout;
    EditText location, officehour;
    String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        Log.d("CoursesActivity Startd", "onCreate: here we are");

        courseTitleTextView = (TextView) findViewById(R.id.courseTitleTextView);
        courseOfficeHoursTextView = (TextView) findViewById(R.id.courseOfficeHoursTextView);
        courseLocationTextView = (TextView) findViewById(R.id.courseLocationTextView);
        courseProfessorTextView = (TextView) findViewById(R.id.courseProfessorTextView);
        btnSave = (Button) findViewById(R.id.saveCourse);
        courseDescriptionTextView = (TextView) findViewById(R.id.courseDescriptionTextView);
        editLayout = (LinearLayout) findViewById(R.id.edit_layout);
        editLayout.setVisibility(LinearLayout.GONE);


        // Get intent that created current Activity
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        courseName = (String) intentBundle.get("name");
        /*HashMap<String, String> courseContent = (HashMap<String, String>) intentBundle.get("content");*/

        HashMap<String, HashMap<String, String>> courseContent = (HashMap<String, HashMap<String, String>>) intentBundle.get("content");

        Log.d("CoursesActivity", "onCreate: " + courseContent);

        String key = courseContent.keySet().toArray()[0].toString();

        final long userId = (Long) intentBundle.get("user");
        final String courseTitle = courseContent.get(key).get("courseTitle");
        final String courseDescription = courseContent.get(key).get("courseDescription");

        setTitle(courseName);
        updateLabelsForCourse(courseContent);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo add the course id to the list of courses under the current student id
                //if the student id is 0, skip
                if (userId == 0) {
                    return;
                }
                // Get reference to database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                // Get reference to specific users class list in the db
                DatabaseReference classRef = databaseReference.child("users")
                        .child(Long.toString(userId))
                        .child("classes");

                // sets the course name and description
                classRef.child(courseTitle).setValue(courseDescription);
            }
        });
        editCourseinfo();
    }


    /*private void updateLabelsForCourse(HashMap<String, String> courseContent) {*/
    private void updateLabelsForCourse(HashMap<String, HashMap<String, String>> courseContent) {

        String key = courseContent.keySet().toArray()[0].toString();

        String courseTitle = "N/A";
        if (courseContent.get(key).containsKey("courseTitle")) {
            courseTitle = courseContent.get(key).get("courseTitle");
        }

        String courseProfessor = "N/A";
        if (courseContent.get(key).containsKey("professor")) {
            courseProfessor = courseContent.get(key).get("professor");
        }

        String courseLocation = "N/A";
        if (courseContent.get(key).containsKey("courseLocation")) {
            courseLocation = courseContent.get(key).get("courseLocation");
        }

        String courseOfficeHours = "N/A";
        if (courseContent.get(key).containsKey("officeHours")) {
            courseOfficeHours = courseContent.get(key).get("officeHours");
        }

        String courseDescription = "N/A";
        if (courseContent.get(key).containsKey("courseDescription")) {
            courseDescription = courseContent.get(key).get("courseDescription");
        }

        String courseLectureTimes = "N/A";
        if (courseContent.get(key).containsKey("lectureTimes")) {
            courseLectureTimes = courseContent.get(key).get("lectureTimes");
        }

        courseTitleTextView.setText(courseTitle);
        courseProfessorTextView.setText(courseProfessor);
        courseLocationTextView.setText(courseLocation);
        courseOfficeHoursTextView.setText(courseOfficeHours);
        courseDescriptionTextView.setText(courseDescription);
    }

    // Edit office hours and location
    private void editCourseinfo() {
        edit = (Button) findViewById(R.id.edit);
        // Edit button. Show the editLayout or not.
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editLayout.getVisibility() == View.GONE)
                    editLayout.setVisibility(LinearLayout.VISIBLE);
                else editLayout.setVisibility(LinearLayout.GONE);
            }
        });

        update = (Button) findViewById(R.id.update);
        location = (EditText) findViewById(R.id.edit_location);
        officehour = (EditText) findViewById(R.id.edit_officehour);
        //TO DO: update the data to database
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String new_loc = location.getText().toString();
                final String new_of = officehour.getText().toString();

                // Get reference to database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                // Get reference to specific office hour field in the db
                DatabaseReference classofRef = databaseReference.child("classes")
                        .child(courseName)
                        .child("officeHour");

                // sets the office hour of the course
                classofRef.setValue(new_of);

                // Get reference to specific location field in the db
                DatabaseReference classlocRef = databaseReference.child("classes")
                        .child(courseName)
                        .child("location");

                // sets the office hour of the course
                classlocRef.setValue(new_loc);

            }
        });
    }
}
