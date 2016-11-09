package com.example.verasy.officehours;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by verasy on 10/25/16.
 */

public class CoursesFragment extends Fragment{
    TextView courseId, courseName, officeHour, Location, Instructor, description;

    public CoursesFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragment, container, false);

        courseId = (TextView)view.findViewById(R.id.course_id);
        courseName = (TextView)view.findViewById(R.id.course_name);
        officeHour = (TextView)view.findViewById(R.id.office_hour);
        Location = (TextView)view.findViewById(R.id.location);
        Instructor = (TextView)view.findViewById(R.id.instructor);
        description =(TextView)view.findViewById(R.id.discription);

        courseId.setText("EC591");
        courseName.setText("Mobile Application Development");
        Instructor.setText("Shereif El-Sheikh");
        Location.setText("64 Commington Ave Room 241");
        officeHour.setText("Wednesday, 2:30-4:30 PM and by Appt");
        description.setText("This is a CS course.");

        return view;

    }
}
