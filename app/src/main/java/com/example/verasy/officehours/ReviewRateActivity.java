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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ReviewRateActivity extends AppCompatActivity {

    private float rates;
    private String comments;
    private Button leaveReview;
    private EditText comment;
    private RatingBar profRatBar;

    ListView reviewlist;

    private DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rate);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        profRatBar = (RatingBar)findViewById(R.id.ratingBar);
        comment = (EditText)findViewById(R.id.prof_comment);
        leaveReview = (Button)findViewById(R.id.button);

        reviewlist = (ListView)findViewById(R.id.reviewlist);
        ArrayList<ReviewObject> objects = new ArrayList<ReviewObject>();
        ReviewObject item1 = new ReviewObject("Good",5f);
        objects.add(item1);
        CustomAdapter customAdapter = new CustomAdapter(this, objects);
        reviewlist.setAdapter(customAdapter);

        profRatBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rates = profRatBar.getRating();
            }
        });

        leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comments = comment.getText().toString();
                writeNewComRate("review2","Shereif",rates,comments);
            }
        });
    }

    public void writeNewComRate(String reviewId, String prof_name, float rating, String comment){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        review review = new review(prof_name,rating,comment);
        mDatabase.child("review").child(reviewId).setValue(review);
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
