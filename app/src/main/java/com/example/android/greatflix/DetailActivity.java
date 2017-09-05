package com.example.android.greatflix;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.greatflix.objects.Movies;
import com.example.android.greatflix.utilities.JsonUtils;
import com.example.android.greatflix.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.greatflix.utilities.NetworkUtils.buildDetailUrl;

public class DetailActivity extends AppCompatActivity {

    private String mPosterPath;
    private String mMovieTitle;
    private String mMovieId;
    private String mMovieReview;

    private String mReleaseDate;
    private String mReviewScore;
    private String mOverview;


    private TextView mMovieTitleTextView;
    private TextView mMovieReviewTextView;

    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;
    private TextView mReviewTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mImageView = (ImageView) findViewById(R.id.iv_detail);
        mMovieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mOverviewTextView = (TextView) findViewById(R.id.tv_overview);
        mReviewTextView = (TextView) findViewById(R.id.tv_ratings);
        mMovieReviewTextView = (TextView) findViewById(R.id.tv_review);




        Intent intentThatStartedTheActivity = getIntent();

        if(intentThatStartedTheActivity!=null){
            if(intentThatStartedTheActivity.hasExtra("posterPath")){
                mPosterPath = intentThatStartedTheActivity.getStringExtra("posterPath");


                String MoviePosterPath = mPosterPath;

                Uri uri = Uri.parse(NetworkUtils.buildMoviePosterPath(MoviePosterPath).toString());
                ImageView ivBasicImage = mImageView;
                Context context = ivBasicImage.getContext();
                Picasso.with(context).load(uri).resize(342,514).centerInside().into(ivBasicImage);

            }if (intentThatStartedTheActivity.hasExtra("movieTitle")){
                mMovieTitle = intentThatStartedTheActivity.getStringExtra("movieTitle");
                mMovieTitleTextView.setText(mMovieTitle);
            }
            if (intentThatStartedTheActivity.hasExtra("movieId")){
                mMovieId = intentThatStartedTheActivity.getStringExtra("movieId");
                URL TrailerUrl = NetworkUtils.buildTrailerUrl(mMovieId);

                Intent TrailerIntent = new Intent();

                URL ReviewUrl = NetworkUtils.buildReviewUrl(mMovieId);

                new ReviewAsyncTask().execute(ReviewUrl);
                mMovieReviewTextView.setText(mMovieReview);
                Log.d("detail", "review " + mMovieReview);
            }
            if (intentThatStartedTheActivity.hasExtra("movieReleaseDate")){
                mReleaseDate = intentThatStartedTheActivity.getStringExtra("movieReleaseDate");
                mReleaseDateTextView.setText(mReleaseDate);
            }if (intentThatStartedTheActivity.hasExtra("movieRating")){
                mReviewScore = intentThatStartedTheActivity.getStringExtra("movieRating");
                mReviewTextView.setText(mReviewScore);
            }if (intentThatStartedTheActivity.hasExtra("movieOverview")){
                mOverview = intentThatStartedTheActivity.getStringExtra("movieOverview");
                mOverviewTextView.setText(mOverview);
            }


        }

    }
    private class ReviewAsyncTask extends AsyncTask<URL,Void,String>{
        @Override
        protected String doInBackground(URL... urls) {
            URL ReviewUrl = makeMovieSearchQuery();
            try {
                String UrlResponse = NetworkUtils.getResponseFromHttpUrl(ReviewUrl);
                mMovieReview = JsonUtils.getReviewContentFromResponse(UrlResponse);

                Log.d("detail", "onCreate: " +mMovieReview);

                return mMovieReview;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    private URL makeMovieSearchQuery(){
        URL movieQueryUrl = NetworkUtils.buildReviewUrl(mMovieId);

        return movieQueryUrl;


    }
}
