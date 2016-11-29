package com.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.Constant;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.R;

import java.util.List;

/**
 * Created by sagar on 21/11/16.
 */

public class CustomAdapter extends ArrayAdapter<Movie> {
    private Context mContext;

    public CustomAdapter(Context context, List<Movie> list) {
        super(context, 0, list);
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie=getItem(position);
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.movieposter,parent,false);
        }
        ImageView imageView= (ImageView) convertView.findViewById(R.id.img);
        Picasso.with(mContext).load(Constant.getImageUrl(movie.getMoviePoster())).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(imageView);

        return convertView;
    }


}
