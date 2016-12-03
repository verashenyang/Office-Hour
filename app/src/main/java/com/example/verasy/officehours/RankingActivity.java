package com.example.verasy.officehours;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

/**
 * Created by verasy on 11/23/16.
 */

public class RankingActivity extends Activity {
    ListView listview;
    TextView tv1;
    HashMap<String, Object> databaseEntries = new HashMap<>();
    private DatabaseReference mDatabase, profRef;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        tv1 = (TextView) findViewById(R.id.tv1);
        listview = (ListView) findViewById(R.id.ranking_list);

        //Get reference to db
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Get reference to reviews
        profRef = mDatabase.child("reviews");

        //Store professor and the corresponding rating array index in prof_index
        final Map<String, ArrayList<String>> prof_list = new HashMap<>();

        //Store professor ranking
        final ArrayList<professor> list = new ArrayList<>();


        profRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                tv1.setText("The 10 best rated professors:");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String prof = snapshot.getKey();

                    //put prof to database
                    prof_list.put(prof, new ArrayList<String>());

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        //put rating to professor's rating list
                        String rating = snapshot1.child("rating").getValue().toString();
                        prof_list.get(prof).add(rating);
                    }
                }

                //Calculate the average rating of a professor
                for (Map.Entry<String, ArrayList<String>> entry : prof_list.entrySet()) {
                    float sum = 0;
                    float average = 0;
                    for (String enrty : entry.getValue()) {
                        sum += Float.valueOf(enrty);
                    }
                    average = sum / entry.getValue().size();
                    list.add(new professor(entry.getKey(), average));
                }

                //Sort the professors' ratings
                Collections.sort(list, new Comparator<professor>() {
                    @Override
                    public int compare(professor o1, professor o2) {
                        if ((o1.average_rating - o2.average_rating) > 0)
                            return -1;
                        else return 1;
                    }
                });

                //Get the professors' ranking
                ArrayList<String> ranking = new ArrayList<String>();
                for (professor prof : list) {
                    ranking.add(prof.getProfName());
                }

                final ArrayAdapter adapter = new ArrayAdapter(RankingActivity.this, android.R.layout.simple_list_item_1, ranking.subList(0,10));
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //
                        final String profClick = (String) listview.getItemAtPosition(position);
                        Log.e("selection", profClick);
                        DatabaseReference professor = mDatabase.child("professors").child(profClick);

                        // Add single value event listener for professor type
                        ValueEventListener professorsListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Store professors in professors dictionary
                                databaseEntries.put(profClick, dataSnapshot.getValue());
                                Log.e("LOG1", databaseEntries.get(profClick) + "hello");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("ExceptionTag", "Cannot get professors from firebase");
                            }
                        };
                        professor.addListenerForSingleValueEvent(professorsListener);

                        //will launch the activity
                        Runnable mLaunchTask = new Runnable() {
                            public void run() {
                                Intent professorIntent = new Intent(RankingActivity.this, ProfessorActivity.class);
                                professorIntent.putExtra("name", profClick);
                                professorIntent.putExtra("content", (HashMap<String, String>) databaseEntries.get(profClick));
                                startActivity(professorIntent);
                            }
                        };

                        //cause some delay to wait for result from database
                        mHandler.postDelayed(mLaunchTask, 200);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled", databaseError.getMessage());
            }
        });
    }

    //Object professor
    class professor {
        private float average_rating;
        private String prof;

        public professor(String prof, Float ave) {
            this.prof = prof;
            this.average_rating = ave;
        }

        public String getProfName() {
            return prof;
        }

        public float getProfRating() {
            return average_rating;
        }

    }
}
