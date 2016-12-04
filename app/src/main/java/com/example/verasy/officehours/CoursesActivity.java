package com.example.verasy.officehours;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;

/* CoursesActivity.java
 * Activity representing the profile page of a given professor
 * Expects: HashMap<String, HashMap<String, String>> "content", Long "user", String "name"
 * Created by Team Emu for BU CAS CS591 E1 Fall 2016
 */

public class CoursesActivity extends AppCompatActivity {

    // MARK: Outlets

    TextView courseNameTextView, courseTitleTextView, lectureLocationTextView, lectureTimeTextView, professorTextView, officeHourLocationTextView, officeHourTextView, courseDescriptionTextView;
    Button editButton, saveCourseButton, updateCourseInfoButton;
    LinearLayout editLayout;
    EditText officeHourEditText, officeHourLocationEditText;

    // MARK: Instance Variables

    String courseCode;
    Long userID;
    HashMap<String, String> courseData;

    // MARK: Initialization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        // Get bundle from intent that created current activity
        Bundle bundle = getIntent().getExtras();

        // Set up current userID and courseCode
        userID = (Long) bundle.get("user");
        courseCode = (String) bundle.get("name");

        // Set up view
        setupView();

        /* Get latest content for current course and update interface.
           Data is organized by timestamp in case we need to roll back.
           getLatestCourseDataFromContent(Object) returns the data relating
           to the most recent timestamp. */
        courseData = getLatestCourseDataFromContent(bundle.get("content"));
        updateLabelsForCourse();

        // Set up buttons
        setupEditButtons();
        setupSaveButton();
    }

    // MARK: View Layout

    private void setupView() {
        // Set up the outlets from the Layout
        courseNameTextView = (TextView) findViewById(R.id.courseNameTextView);
        courseTitleTextView = (TextView) findViewById(R.id.courseTitleTextView);
        lectureLocationTextView = (TextView) findViewById(R.id.lectureLocationTextView);
        lectureTimeTextView = (TextView) findViewById(R.id.lectureTimeTextView);
        professorTextView = (TextView) findViewById(R.id.professorTextView);
        officeHourLocationTextView = (TextView) findViewById(R.id.officeHourLocationTextView);
        officeHourTextView = (TextView) findViewById(R.id.officeHourTextView);
        courseDescriptionTextView = (TextView) findViewById(R.id.courseDescriptionTextView);

        editButton = (Button) findViewById(R.id.editButton);
        saveCourseButton = (Button) findViewById(R.id.saveCourseButton);
        updateCourseInfoButton = (Button) findViewById(R.id.updateCourseInfoButton);

        editLayout = (LinearLayout) findViewById(R.id.editLayout);

        officeHourEditText = (EditText) findViewById(R.id.officeHourEditText);
        officeHourLocationEditText = (EditText) findViewById(R.id.officeHourLocationEditText);

        // Update background color of save course and update course info buttons
        saveCourseButton.getBackground().setColorFilter(0xFF5baaf4, PorterDuff.Mode.MULTIPLY);
        updateCourseInfoButton.getBackground().setColorFilter(0xFF5baaf4, PorterDuff.Mode.MULTIPLY);
    }

    private void updateLabelsForCourse() {
        // Check if course name/ code exists
        String courseName = "Course Name Not Available";
        if (!courseCode.equals("")) {
            courseName = courseCode;
        }

        // Check if course title exists
        String courseTitle = "Course Title Not Available";
        if (courseData.containsKey("courseTitle")) {
            courseTitle = courseData.get("courseTitle");
        }

        // Check if lecture location exists
        String lectureLocation = "Lecture Location Not Available";
        if (courseData.containsKey("courseLocation")) {
            lectureLocation = courseData.get("courseLocation");
        }

        // Check if lecture time exists
        String lectureTime = "Lecture Time Not Available";
        if (courseData.containsKey("lectureTimes")) {
            lectureTime = courseData.get("lectureTimes");
        }

        // Check if professor exists
        String professor = "Professor Not Available";
        if (courseData.containsKey("professor")) {
            professor = courseData.get("professor");
        }

        // Check if office hours location exists
        String officeHourLocation = "Office Hour Location Not Available";
        if (courseData.containsKey("officeHoursLocation")) {
            officeHourLocation = courseData.get("officeHoursLocation");
        }

        // Check if office hours exists
        String officeHour = "Office Hour Time Not Available";
        if (courseData.containsKey("officeHours")) {
            officeHour = courseData.get("officeHours");
        }

        // Check if course description exists
        String courseDescription = "Course Description Not Available";
        if (courseData.containsKey("courseDescription")) {
            courseDescription = courseData.get("courseDescription");
        }

        // Update labels
        courseNameTextView.setText(courseName);
        courseTitleTextView.setText(courseTitle);
        lectureLocationTextView.setText(lectureLocation);
        lectureTimeTextView.setText(lectureTime);
        professorTextView.setText(professor);
        officeHourLocationTextView.setText(officeHourLocation);
        officeHourTextView.setText(officeHour);
        courseDescriptionTextView.setText(courseDescription);
    }

    // MARK: Interactivity

    private void setupEditButtons() {
        // If userID is not 0 (i.e. not guest login), setup on-click listeners. Otherwise disable edit button
        if (userID != 0) {
            // Set on-click listener for "EDIT" button
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the InputMethodManager for the keyboard (to show or dismiss)
                    InputMethodManager keyboardManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    /* If the edit layout is not visible, make it visible and bring up keyboard
                       Otherwise, hide edit layout and keyboard */
                    if (editLayout.getVisibility() == View.GONE) {
                        editLayout.setVisibility(View.VISIBLE);

                        keyboardManager.showSoftInput(officeHourEditText, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        editLayout.setVisibility(View.GONE);

                        keyboardManager.hideSoftInputFromWindow(editLayout.getWindowToken(), 0);
                    }
                }
            });

            // Set on-click listener for "UPDATE" button
            updateCourseInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the updated office hours location and time
                    String updatedOfficeHourLocation = officeHourLocationEditText.getText().toString();
                    String updatedOfficeHour = officeHourEditText.getText().toString();

                    // Get the current UNIX timestamp (used to back up data)
                    String currentTimestamp = ((Long) (System.currentTimeMillis() / 1000)).toString();

                    // Get a copy of the current course data to update relevant information
                    HashMap<String, String> updatedCourseData = new HashMap<String, String>(courseData);

                    // If new data is empty, remove key from map. Otherwise put new data for correct key
                    if (officeHourEditText.getText().toString().equals("")) {
                        updatedCourseData.remove("officeHours");
                    } else {
                        updatedCourseData.put("officeHours", officeHourEditText.getText().toString());
                    }
                    if (officeHourLocationEditText.getText().toString().equals("")) {
                        updatedCourseData.remove("officeHoursLocation");
                    } else {
                        updatedCourseData.put("officeHoursLocation", officeHourLocationEditText.getText().toString());
                    }

                    // Store the value in the database under the current timestamp
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("classes").child(courseCode).child(currentTimestamp).setValue(updatedCourseData);

                    // Update labels on current view
                    courseData = updatedCourseData;
                    updateLabelsForCourse();

                    // Remove edit view, keyboard, and text
                    editButton.callOnClick();
                    officeHourEditText.setText("");
                    officeHourLocationEditText.setText("");
                }
            });
        } else {
            editButton.setEnabled(false);
        }
    }

    private void setupSaveButton() {
        // If userID is not 0 (i.e. not guest login), setup on-click listener. Otherwise disable button
        if (userID != 0) {
            // Set on-click listener for "SAVE" button
            saveCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // If userID is 0 (i.e. guest login) abort, we cannot save courses for anonymous user
                    if (userID == 0) {
                        return;
                    }

                    // Get reference to database and save this course for the current user
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("users").child(userID.toString()).child("classes").child(courseCode).setValue(courseData.get("courseTitle"));
                }
            });
        } else {
            saveCourseButton.setEnabled(false);
        }
    }

    // MARK: Data Manipulation

    // Method to get course data of type courseName:{title, desc, ...} from courseName:{timestamp:{title, desc, ...}}
    private HashMap<String, String> getLatestCourseDataFromContent(Object content) {
        // Cast content to HashMap, get keyset, and sort ascending
        HashMap<String, Object> contentDict = (HashMap<String, Object>) content;
        Object[] keyset = contentDict.keySet().toArray();
        Arrays.sort(keyset);

        // Return the data matching the last timestamp (most recent data)
        HashMap<String, String> courseContent = (HashMap<String, String>) contentDict.get(keyset[keyset.length - 1]);
        return courseContent;
    }

}