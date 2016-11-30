package com.example.verasy.officehours;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class ReviewRateActivity extends AppCompatActivity {

    private float rates;
    private String comments;
    private Button leaveReview;
    private EditText comment;
    private RatingBar profRatBar;
    private RatingBar average_rating;

    ListView reviewList;

    private DatabaseReference mDatabase;
    HashMap<String, Object> databaseEntries = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rate);

        // Get reference to the FireBase Real Time Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        profRatBar = (RatingBar)findViewById(R.id.ratingBar);
        comment = (EditText)findViewById(R.id.prof_comment);
        leaveReview = (Button)findViewById(R.id.button);
        average_rating = (RatingBar)findViewById(R.id.average_rating);
        reviewList = (ListView)findViewById(R.id.reviewlist);


        // using an ArrayList to hold all matched tuple
        final ArrayList<ReviewObject> objects = new ArrayList<ReviewObject>();

        //Get reference to the FireBase about dictionary "reviews"
        DatabaseReference reviewsRef = mDatabase.child("reviews");

        // Add single value event listener for "reviews" dictionary
        ValueEventListener reviewListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Store all classes in classes dictionary first
                databaseEntries = (HashMap<String, Object>) dataSnapshot.getValue();

                //create an object to get total score of all rating related to the professor
                float total = 0f;

                // Find search matches from keys
                for(String key: databaseEntries.keySet()){

                    HashMap<String,String> content = (HashMap<String, String>)databaseEntries.get(key);
                    if(content.get("professor")!=null && content.get("professor").equals("Evimaria Terzi")){
                        //every iteration will create a matched item object to hold the data
                        ReviewObject item = new ReviewObject(content.get("professor"),content.get("comment"),null);

                        //get the rating and add it to created object and get the total score of all ratings
                        if(content.get("rating")!=null){
                            item.rating = Float.valueOf(content.get("rating"));
                            total += Float.valueOf(content.get("rating"));
                        } else {
                            item.rating = 0f;
                        }
                        objects.add(item);
                    }
                }
                //set the rating bar of average_rating to acquired calculation result
                average_rating.setRating(total/objects.size());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reviewsRef.addValueEventListener(reviewListener);

        //set the adapter with the ArrayList and customized adapter layout
        CustomAdapter customAdapter = new CustomAdapter(this, objects);
        reviewList.setAdapter(customAdapter);

        // Get the professor rating from RatingBar
        profRatBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rates = profRatBar.getRating();
            }
        });

        // Click the button to write data into firebase
        leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comments = comment.getText().toString();
                writeNewComRate("Shereif",rates,comments);
            }
        });
    }

    // method for writing comment and rating of the professor into FireBase
    public void writeNewComRate(String prof_name, float rating, String comment){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ReviewObject review = new ReviewObject(prof_name,comment,rating);
        mDatabase.child("reviews").push().setValue(review);
    }


    //CustomAdapter. A row of ListView has a TextView to show comment and a RatingBar.
    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<ReviewObject> objects;

        private class ViewHolder {
            TextView comment;
            RatingBar rb;
        }

        public CustomAdapter(Context context, ArrayList<ReviewObject> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public ReviewObject getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.reviewlist_row, null);
                holder.comment = (TextView) convertView.findViewById(R.id.comment_row);
                holder.rb = (RatingBar) convertView.findViewById(R.id.rating_row);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.comment.setText(objects.get(position).getComment());
            holder.rb.setRating(objects.get(position).getRating());
            return convertView;
        }
    }

}
