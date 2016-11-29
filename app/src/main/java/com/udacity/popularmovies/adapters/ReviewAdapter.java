package com.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.popularmovies.models.ReviewModel;
import com.udacity.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by sagar on 24/11/16.
 */

public class ReviewAdapter extends ArrayAdapter<ReviewModel> {
    private Context mContext;
    private ArrayList<ReviewModel> reviewModels;
    public ReviewAdapter(Context context, ArrayList<ReviewModel> objects) {
        super(context, 0, objects);
        mContext=context;
        reviewModels=objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewModel review=getItem(position);
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.row_review,parent,false);
        }
        TextView textAuthor= (TextView) convertView.findViewById(R.id.text_review_author);
        textAuthor.setText(review.getAuthor());

        TextView textContent=(TextView)convertView.findViewById(R.id.text_review_content);
        textContent.setText(review.getContent());


        return convertView;
    }

    public ArrayList<ReviewModel> getItems() {
        return reviewModels;
    }
}
