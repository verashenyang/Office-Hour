package com.example.verasy.officehours;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import static com.example.verasy.officehours.R.id.btnGoToSearch;

/* SearchFragment.java
 * OfficeHours app
 * CS591 Final Project - Team Emu
 */

// Interface for search fragment to notify activity of searches
interface SearchListener {
    void searchClass(String searchString);
    void searchProf(String searchString);
    void topTenButtonDidClick();
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
        final RadioButton radioProfs = (RadioButton)view.findViewById(R.id.radioProfs);
        final RadioButton radioClasses = (RadioButton)view.findViewById(R.id.radioClasses);
        final Button btnTop = (Button)view.findViewById(R.id.btnTop);

        final Button btnSearch = (Button)view.findViewById(R.id.btn_search);

        btnSearch.getBackground().setColorFilter(0xFF5baaf4, PorterDuff.Mode.MULTIPLY);
        btnTop.getBackground().setColorFilter(0xFF5baaf4, PorterDuff.Mode.MULTIPLY);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = edtSearch.getText().toString();
                if(radioClasses.isChecked()){
                    listener.searchClass(searchString);
                } else {
                    listener.searchProf(searchString);
                }
                edtSearch.setText("");
            }
        });

        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.topTenButtonDidClick();
            }
        });


        return view;
    }

}