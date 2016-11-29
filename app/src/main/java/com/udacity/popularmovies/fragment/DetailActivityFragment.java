package com.udacity.popularmovies.fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.adapters.ReviewAdapter;
import com.udacity.popularmovies.adapters.TrailerAdapter;
import com.udacity.popularmovies.asyncTask.ManageFavouriteAsyncTask;
import com.udacity.popularmovies.Constant;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.models.ReviewModel;
import com.udacity.popularmovies.models.TrailerModel;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.webService.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailActivityFragment extends Fragment {

    @Bind(R.id.movie_title)
    TextView movieTitle;
    @Bind(R.id.movie_desc)
    TextView movieDesc;
    @Bind(R.id.movie_release_date)
    TextView movieReleaseDate;
    @Bind(R.id.movie_poster)
    ImageView imageMoviePoster;
    @Bind(R.id.favorite)
    Button buttonFavourite;
    @Bind(R.id.movie_rating)
    TextView movieRatingTextView;
    @Bind(R.id.ratingBar1)
    RatingBar ratingBar;
    @Bind(R.id.trailer_list)
    ListView trailerList;
    @Bind(R.id.review_list)
    ListView reviewList;


    private Movie pojo;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private ArrayList<TrailerModel> trailerModelList;
    private ArrayList<ReviewModel> reviewModelList;
    private static final String STATE_VIDEOS="VIDEOS";
    private static final String STATE_REVIEWS="REVIEWS";
    public DetailActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_detail_activity, container, false);
        ButterKnife.bind(this,rootView);

        Bundle bundle=getArguments();
        if (bundle==null){
            rootView.setVisibility(View.INVISIBLE);
            return rootView;
        }
        pojo=bundle.getParcelable(Constant.MOVIE_TAG);

        if (pojo!=null){
            movieTitle.setText(pojo.getTitle());
            movieDesc.setText(pojo.getOverview());
            movieReleaseDate.setText(pojo.getReleaseDate());
            movieRatingTextView.setText(String.valueOf(pojo.getRating()));
            Picasso.with(getContext()).load(Constant.getImageUrl(pojo.getMoviePoster()))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageMoviePoster);

            ratingBar.setMax(5);
            ratingBar.setStepSize(0.01f);
            ratingBar.setRating(Float.parseFloat(String.valueOf(pojo.getRating()/2)));
            ratingBar.invalidate();
        }

        buttonFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"This Feature is Part of Next Stage",Toast.LENGTH_SHORT).show();
            }
        });

        trailerModelList= new ArrayList<>();
        reviewModelList =new ArrayList<>();


        trailerAdapter=new TrailerAdapter(getContext(),  trailerModelList);
        reviewAdapter=new ReviewAdapter(getContext(), reviewModelList);

        reviewList.setAdapter(reviewAdapter);
       // reviewList.setEmptyView(rootView.findViewById(R.id.emptyViewReview));
        trailerList.setAdapter(trailerAdapter);
      //  trailerList.setEmptyView(rootView.findViewById(R.id.emptyViewTrailer));

        if(savedInstanceState!=null && savedInstanceState.containsKey(STATE_VIDEOS)){
            ArrayList<TrailerModel> tr=savedInstanceState.getParcelableArrayList(STATE_VIDEOS);
            trailerAdapter.addAll(tr);
        }else {
            if (Constant.isOnline(getContext())){
                new AsyncTaskTrailer().execute(Constant.getTrailerUrl(pojo.getId()));
            }
        }

        if(savedInstanceState!=null && savedInstanceState.containsKey(STATE_REVIEWS)){

            ArrayList<ReviewModel> rev=savedInstanceState.getParcelableArrayList(STATE_REVIEWS);
            reviewAdapter.addAll(rev);
        }else {

            if(Constant.isOnline(getContext())){
                new AsyncTaskReview().execute(Constant.getReviewUrl(pojo.getId()));
            }
        }



        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String youtubeVideoId = trailerModelList.get(position).getKey();

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeVideoId));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + youtubeVideoId));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });
        new ManageFavouriteAsyncTask(getContext(),false,pojo,buttonFavourite).execute();
        buttonFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ManageFavouriteAsyncTask(getContext(),true,pojo,buttonFavourite).execute();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<TrailerModel> trailers=trailerAdapter.getItems();
        if (trailers!=null && !trailers.isEmpty()){

            outState.putParcelableArrayList(STATE_VIDEOS,trailers);
        }
        ArrayList<ReviewModel> reviews=reviewAdapter.getItems();
        if (reviews!=null && !reviews.isEmpty()){
            outState.putParcelableArrayList(STATE_REVIEWS,reviews);
        }

    }

    private class AsyncTaskReview extends AsyncTask<String,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(String... url) {
            return JSONParser.getDataFromWeb(url[0]);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            parseReviewJson(object);
        }

        private void parseReviewJson(JSONObject object){

            try {
                if(object==null){
                    reviewAdapter.add(new ReviewModel("","",getString(R.string.Review_Not_Available)));
                    return;
                }
                JSONArray array=object.getJSONArray("results");
                if(array.length()<=0){
                    reviewAdapter.add(new ReviewModel("","",getString(R.string.Review_Not_Available)));
                    return;
                }
                for(int i=0;i<array.length();i++){
                    JSONObject jsonObject=array.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    String author=jsonObject.getString("author");
                    String content=jsonObject.getString("content");
                    ReviewModel reviewModel=new ReviewModel(id,author,content);
                    reviewModelList.add(reviewModel);
                    reviewAdapter.add(reviewModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AsyncTaskTrailer extends AsyncTask<String,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(String... url) {
            return JSONParser.getDataFromWeb(url[0]);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            parseTrailerJson(object);
        }

        private void parseTrailerJson(JSONObject object){

            try {
                if(object==null){
                    trailerAdapter.add(new TrailerModel("","",getString(R.string.Trailer_Not_Available)));
                    return;
                }
                JSONArray array=object.getJSONArray("results");
                if(array.length()<=0){
                    trailerAdapter.add(new TrailerModel("","",getString(R.string.Trailer_Not_Available)));
                    return;
                }
                for(int i=0;i<array.length();i++){
                    JSONObject jsonObject=array.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    String key=jsonObject.getString("key");
                    String name=jsonObject.getString("name");
                    TrailerModel trailerModel=new TrailerModel(id,key,name);
                    trailerModelList.add(trailerModel);
                    trailerAdapter.add(trailerModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
