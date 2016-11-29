package com.udacity.popularmovies.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.popularmovies.Constant;
import com.udacity.popularmovies.fragment.DetailActivityFragment;
import com.udacity.popularmovies.R;

public class DetailActivity extends AppCompatActivity {
   private static final String DETAIL="DetailFragMent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle=new Bundle();
        bundle.putParcelable(Constant.MOVIE_TAG,
                getIntent().getParcelableExtra(Constant.MOVIE_TAG));

        DetailActivityFragment fragment=new DetailActivityFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container,fragment,DETAIL).commit();

    }



}
