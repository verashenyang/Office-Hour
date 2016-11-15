package com.example.verasy.officehours;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by verasy on 10/25/16.
 */

interface SearchListener {
    void search(String string);
}

public class SearchFragment extends Fragment{
    private SearchListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SearchListener) context;
        } catch (ClassCastException castException) {
            // The actvity does not implement SearchListener
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        final EditText edtSearch = (EditText)view.findViewById(R.id.search_input);

        Button btnSearchClass = (Button)view.findViewById(R.id.btn_search_class);
        btnSearchClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("term", edtSearch.getText().toString());
                intent.putExtra("type", "classes");
//                intent.putExtra("user", getArguments().getLong("user"));
                startActivity(intent);*/

                listener.search(edtSearch.getText().toString());
            }
        });

        Button btnSearchProf = (Button)view.findViewById(R.id.btn_search_prof);
        btnSearchProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("term", edtSearch.getText());
                intent.putExtra("type", "prof");
//                intent.putExtra("user", getArguments().getLong("user"));
                startActivity(intent);
            }
        });

        return view;
    }
}
