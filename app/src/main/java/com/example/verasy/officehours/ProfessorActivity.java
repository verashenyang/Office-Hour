package com.example.verasy.officehours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

/* ProfessorActivity.java
 * Activity representing the profile page of a given professor
 * Expects: HashMap<String, String> "content", Long "user", String "name"
 * Created by Team Emu for BU CAS CS591 E1 Fall 2016
 */

public class ProfessorActivity extends AppCompatActivity {

    // MARK: Outlets

    ImageView professorImageView;
    TextView nameTextView, officeTextView;
    Button emailButton, ratingsButton;
    ListView coursesListView;

    // MARK: Instance Variables

    Long userId;
    String professorName;
    HashMap<String, String> professorData;
    HashMap<String, Object> databaseListing = new HashMap<>();

    // MARK: Initialization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        // Get intent that created current Activity
        Bundle bundle = getIntent().getExtras();

        // Set current userID, professorName, and professorData
        userId = (Long) bundle.get("user");
        professorName = (String) bundle.get("name");
        professorData = (HashMap<String, String>) bundle.get("content");

        // Set up view
        setupView();
        updateViewForCurrentProfessor();

        // Set up interactivity
        setupListOnClickListener();
    }

    // MARK: View Layout

    private void setupView() {
        // Set up the outlets from the Layout
        professorImageView = (ImageView) findViewById(R.id.professorImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        officeTextView = (TextView) findViewById(R.id.officeTextView);
        emailButton = (Button) findViewById(R.id.emailButton);
        ratingsButton = (Button) findViewById(R.id.ratingsButton);
        coursesListView = (ListView) findViewById(R.id.coursesListView);
    }

    private void updateViewForCurrentProfessor() {
        // Check if professor name exists
        String name = "Name Not Available";
        if (professorName != null && professorName.length() > 0) {
            name = professorName;
        }

        // Check if office location exists
        String office = "Office Location Not Available";
        if (professorData.containsKey("office")) {
            office = professorData.get("office");
        }

        // Check if email exists
        String email = "";
        if (professorData.containsKey("email")) {
            email = professorData.get("email");
        }

        // Check if courses exists
        String courses = "";
        if (professorData.containsKey("classes")) {
            courses = professorData.get("classes");
        }

        // Check if photo exists
        String photo = "";
        if (professorData.containsKey("photo")) {
            photo = professorData.get("photo");
        }

        // Update labels with information
        nameTextView.setText(name);
        officeTextView.setText(office);

        // Enable email button if email
        enableEmailButtonWithEmail(email);

        // Set up courses list view if courses
        setUpCoursesListView(courses);

        // Update professor profile image if image
        updateProfessorImageView(photo);
    }

    private void enableEmailButtonWithEmail(final String email) {
        // Check if email exists, otherwise disable button
        if (!email.equals("")) {

            // Set email button onclick listener that creates an intent
            emailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create implicit intent for sending email, passing in the professor email as destination
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                }
            });
        } else  {
            emailButton.setEnabled(false);
        }
    }

    private void setUpCoursesListView(String courses) {
        // Check if there are courses
        if (!courses.equals("")) {

            // Split courses string into array based on comma ("class1,class2" -> ["class1", "class2"])
            String[] coursesArray = courses.split(",");

            // Create new array adapter for courses list view using the simple_list_item_1 style
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, coursesArray);
            coursesListView.setAdapter(adapter);

            // Create reference to courses in Firebase
            DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference().child("classes");

            // Create listener to download courses data
            ValueEventListener coursesListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Update databaseListing based on returned data from Firebase
                    databaseListing = (HashMap<String, Object>) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ExceptionTag", "Cannot get classes from firebase");
                }
            };
            coursesReference.addListenerForSingleValueEvent(coursesListener);
        }
    }

    private void updateProfessorImageView(String photo) {
        // Check if photo exists, if not keep current placeholder image
        if (!photo.equals("")) {
            // Create reference to image stored in Firebase Storage
            StorageReference photoPathReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cs591-final-project.appspot.com/photos/" + photo);

            // Use Glide (part of Firebase UI) to download image and insert into Image View
            Glide.with(this).using(new FirebaseImageLoader()).load(photoPathReference).into(professorImageView);
        }
    }

    // MARK: Interaction

    private void setupListOnClickListener() {
        // Set up listener to be called when course from ListView is clicked
        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the label of the selected row
                String selection = (String)coursesListView.getItemAtPosition(position);

                // Create intent to the Courses Activity passing in the selected course
                Intent classesIntent = new Intent(ProfessorActivity.this, CoursesActivity.class);
                classesIntent.putExtra("name", selection);
                classesIntent.putExtra("content", (HashMap<String, HashMap<String, String>>) databaseListing.get(selection));
                classesIntent.putExtra("user", userId);
                startActivity(classesIntent);
            }
        });
    }
}