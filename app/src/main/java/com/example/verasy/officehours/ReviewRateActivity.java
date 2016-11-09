package com.example.verasy.officehours;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


public class ReviewRateActivity extends AppCompatActivity {
    private
    ListView reviewlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rate);

        reviewlist = (ListView)findViewById(R.id.reviewlist);
        ArrayList<ReviewObject> objects = new ArrayList<ReviewObject>();
        ReviewObject item1 = new ReviewObject("Good",4f);
        objects.add(item1);
        CustomAdapter customAdapter = new CustomAdapter(this, objects);
        reviewlist.setAdapter(customAdapter);
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
