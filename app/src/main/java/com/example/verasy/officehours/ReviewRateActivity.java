package com.example.verasy.officehours;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class ReviewRateActivity extends AppCompatActivity {

    private float rates;
    private String comments;
    private Button leaveReview;
    private EditText comment;
    private RatingBar profRatBar;
    private RatingBar average_rating;
    private TextView average_score;
    private Button btn_voice;
    private Integer size;

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
        average_score = (TextView)findViewById(R.id.average_score);
        btn_voice = (Button)findViewById(R.id.btn_voice);

        //Get intent from ProfessorActivity
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        //Get the profName by getExtras from intent
        final String profName = (String)intentBundle.get("ProfName");

        // using an ArrayList to hold all matched tuple
        final ArrayList<ReviewObject> objects = new ArrayList<ReviewObject>();

        final CustomAdapter customAdapter = new CustomAdapter(this, objects);
        reviewList.setAdapter(customAdapter);

        //Forward reference to dictionary "reviews" in FireBase
        DatabaseReference reviewsRef = mDatabase.child("reviews");

        //Add value event listener
        //[start lister]
        ValueEventListener reviewListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Get reviews object and use the values to update the UI
                databaseEntries = (HashMap<String,Object>) dataSnapshot.getValue();

                //Create an arrayList to hold every comment and rate about the professor
                //comment and rate is the value of the HashMap, and the index is the key of the HashMap
                ArrayList<HashMap<String,String>> content = new ArrayList<>();

                //To hold the total rating of the professor
                float total = 0f;

                //Refresh the objects arrayList once this method is called again
                objects.clear();

                //Get the data of the professor searched
                for(String key: databaseEntries.keySet()){
                    if(key.equals(profName)){
                        content = (ArrayList<HashMap<String,String>>)databaseEntries.get(key);
                        break;
                        //Once find it, jump out.
                    }
                }

                //adding every comment and rating into tuple object
                for(HashMap<String,String> item: content){
                    if(item.get("comment")!=null && item.get("rating")!=null){
                        float rate = Float.valueOf(String.valueOf(item.get("rating")));
                        ReviewObject tuple = new ReviewObject(profName,item.get("comment"),rate);
                        total += tuple.rating;
                        //append every tuple to the objects arrayList
                        objects.add(tuple);
                    }
                }
                //Get the total size of comments for the professor
                size = objects.size();

                //get the average rating of the professor
                float rate = total/content.size();
                //only keep one decimal of the float number
                DecimalFormat df = new DecimalFormat("#.0");
                //set the rating bar to the average rating
                average_rating.setRating(rate);
                //set the TextView showing the average rating
                average_score.setText(df.format(rate)+"/5");

                //once a new comment is added, this method will automatically renew the data we need
                customAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reviewsRef.addValueEventListener(reviewListener);
        //[end lister]



        // Get the professor rating from RatingBar
        profRatBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rates = profRatBar.getRating();
            }
        });

        // Click the button to write data into FireBase
        leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comments = comment.getText().toString();
                writeNewComRate(profName,rates,comments,size);
                comment.setText("");
                profRatBar.setRating(0f);
            }
        });

        // Clicking the voiceInput button, users could use microphone to write the comment
        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-us");
                //Below is what the user sees before they talk.
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk to Me!");

                try
                {
                    startActivityForResult(i, 999);
                    //the 999 serves as our request code (breadcrumb), so we know what to phish for if we start other activities for results
                }
                catch (ActivityNotFoundException e)
                {
                    //populate a toast when the microphone is not available
                    Toast.makeText(ReviewRateActivity.this, "Microphone not available.  Does you device have a Mic?", Toast.LENGTH_LONG);
                }

            }
        });
    }

    // method for writing comment and rating of the professor into FireBase
    public void writeNewComRate(String prof_name, float rate, String comment, int size){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String newIndex = Integer.toString(size);
        Log.e("hello",newIndex);
        //put the data into correct location of the FireBase
        mDatabase.child("reviews").child(prof_name).child(newIndex).child("rating").setValue(rate);
        mDatabase.child("reviews").child(prof_name).child(newIndex).child("comment").setValue(comment);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && resultCode == RESULT_OK)
        //Ensuring we are following the right bread crumb trail, and that the result is OK.  q&d - better to use a constant, not 999.
        {
            ArrayList<String> txtSpeech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //EXTRA_RESULTS CONTAINS THE INTENT'S RETURNED TEXT
            comment.setText(txtSpeech.get(0));

        }
    }

}
