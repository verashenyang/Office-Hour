package com.example.verasy.officehours;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by verasy on 10/25/16.
 */

public class SearchFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        final EditText edtSearch = (EditText)view.findViewById(R.id.search_input);

        Button btnSearchClass = (Button)view.findViewById(R.id.btn_search_class);
        btnSearchClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("term", edtSearch.getText().toString());
                intent.putExtra("type", "classes");
//                intent.putExtra("user", getArguments().getLong("user"));
                startActivity(intent);
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
