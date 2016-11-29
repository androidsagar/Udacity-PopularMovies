package com.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.popularmovies.models.TrailerModel;
import com.udacity.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by sagar on 24/11/16.
 */

public class TrailerAdapter extends ArrayAdapter<TrailerModel> {
    private Context mContext;
    private ArrayList<TrailerModel> list;
    public TrailerAdapter(Context context, ArrayList<TrailerModel> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.list=objects;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrailerModel trailers=getItem(position);
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.row_trailer,parent,false);
        }

        TextView textTrailer= (TextView) convertView.findViewById(R.id.text_trailer_name);
        textTrailer.setText(trailers.getName());

        return convertView;
    }

    public ArrayList<TrailerModel> getItems() {
        return list;
    }

    public void setList(ArrayList<TrailerModel> list) {
        this.list = list;
    }
}
