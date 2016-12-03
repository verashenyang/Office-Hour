package com.example.verasy.officehours;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by verasy on 11/23/16.
 */

public class RankingActivity extends Activity{
    private DatabaseReference mDatabase,profRef;
    ListView listview;
    TextView tv1;
    HashMap<String, String> databaseEntries = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        tv1 = (TextView)findViewById(R.id.tv1);
        listview = (ListView)findViewById(R.id.ranking_list);

        //Get reference to db
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Get reference to reviews
        profRef = mDatabase.child("reviews");

        //Store professor and the corresponding rating array index in prof_index
        final Map<String,ArrayList<String>> prof_list = new HashMap<>();

        //Store professor ranking
        final ArrayList<professor> list = new ArrayList<>();


        profRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv1.setText("The 10 best rated professors:");
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    GenericTypeIndicator<Map<String,String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                    };
                    Map<String, String> map = snapshot.getValue(genericTypeIndicator);

                    //Get the professor and rating
                    String prof = map.get("professor");
                    String rating = map.get("rating");

                    //If the prof_list contains the professor add the rating to the professor's rating list
                    if(prof_list.containsKey(prof)){
                        prof_list.get(prof).add(rating);
                    //If the prof_list doesn't contain the professor add him/her to the prof_list and then add his/her rating to the rating list
                    }else{
                        prof_list.put(prof,new ArrayList<String>());
                        prof_list.get(prof).add(rating);
                    }
                    Log.e("Prof","The professor is " + prof+" and the rating is "+ rating);
                }

                //Calculate the average rating of a professor
                for (Map.Entry<String, ArrayList<String>> entry : prof_list.entrySet()) {
                    float sum = 0;
                    float average = 0;
                    for(String enrty:entry.getValue()){
                        sum += Float.valueOf(enrty);
                    }
                    average = sum/entry.getValue().size();
                    list.add(new professor(entry.getKey(),average));
                    //Log.e("Average",""+average);
                }

                //Sort the professors' ratings
                Collections.sort(list, new Comparator<professor>() {
                    @Override
                    public int compare(professor o1, professor o2) {
                        if((o1.average_rating - o2.average_rating) > 0)
                            return 1;
                        else return -1;
                    }
                });

                //Get the professors' ranking
                ArrayList<String> ranking = new ArrayList<String>();
                for(professor prof : list) {
                    ranking.add(prof.getProfName());
                }

                ArrayAdapter adapter = new ArrayAdapter(RankingActivity.this, android.R.layout.simple_list_item_1,ranking.subList(0,10));
                listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled", databaseError.getMessage());
            }
        });
    }

    //Object professor
    class professor{
        private float average_rating;
        private String prof;

        public professor(String prof,Float ave){
            this.prof = prof;
            this.average_rating = ave;
        }
        public String getProfName(){
            return prof;
        }
        public float getProfRating() {
            return average_rating;
        }

    }
}
