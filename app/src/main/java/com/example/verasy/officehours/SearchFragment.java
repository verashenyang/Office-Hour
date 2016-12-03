package com.example.verasy.officehours;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/* SearchFragment.java
 * OfficeHours app
 * CS591 Final Project - Team Emu
 */

// Interface for search fragment to notify activity of searches
interface SearchListener {
    void searchClass(String searchString);
    void searchProf(String searchString);
}

public class SearchFragment extends Fragment{

    private SearchListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /* Try to set the parent activity (context) as delegate/ listener for interface
         * If activity does not implement interface, log error */
        try {
            listener = (SearchListener) context;
        } catch (ClassCastException castException) {
            // The actvity does not implement SearchListener
            Log.e("ExceptionTag", "Activity does not implement SearchListener");
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
         /* Try to set the parent activity (context) as delegate/ listener for interface
         * If activity does not implement interface, log error */
        try {
            listener = (SearchListener) activity;
        } catch (ClassCastException castException) {
            // The actvity does not implement SearchListener
            Log.e("ExceptionTag", "Activity does not implement SearchListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search_fragment, container, false);

        // Find edit-text field, search class button, and search prof button in interface
        final EditText edtSearch = (EditText)view.findViewById(R.id.search_input);
        final Button btnSearchClass = (Button)view.findViewById(R.id.btn_search_class);
        final Button btnSearchProf = (Button)view.findViewById(R.id.btn_search_prof);

        // Add on-click listener to search class button, calling interface method
        btnSearchClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text in textfield
                String searchString = edtSearch.getText().toString();

                // Call interface method on activity passing in search string
                listener.searchClass(searchString);
            }
        });

        // Add on-click listener to search prof button, calling interface method
        btnSearchProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text in textfield
                String searchString = edtSearch.getText().toString();

                // Call interface method on activity passing in search string
                listener.searchProf(searchString);
            }
        });

        return view;
    }

}
