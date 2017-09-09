package com.example.android.greatflix;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.greatflix.objects.Movies;
import com.example.android.greatflix.objects.Reviews;
import com.example.android.greatflix.objects.Trailer;
import com.example.android.greatflix.utilities.JsonUtils;
import com.example.android.greatflix.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.greatflix.utilities.NetworkUtils.buildDetailUrl;

public class DetailActivity extends AppCompatActivity {

    private String mPosterPath;
    private String mMovieTitle;
    private String mMovieId;
    private Reviews mMovieReview;

    private String mReleaseDate;
    private String mReviewScore;
    private String mOverview;


    private TextView mMovieTitleTextView;
    private TextView mMovieReviewTextView;
    private TextView mMovieReviewAuthorTextView;

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
        mMovieReviewAuthorTextView = (TextView) findViewById(R.id.tv_review_author);



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

                URL ReviewUrl = NetworkUtils.buildReviewUrl(mMovieId);
                new ReviewAsyncTask().execute(ReviewUrl);

                ImageButton button = (ImageButton) findViewById(R.id.iv_play_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        URL TrailerUrl = NetworkUtils.buildTrailerUrl(mMovieId);
                        new TrailerAsyncTask().execute(TrailerUrl);
                    }
                });
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
    public void addToFavorites(View v){
        ContentValues values = new ContentValues();

        values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_NAME, mMovieTitle);

        Uri uri = getContentResolver().insert(MovieFavoriteContract.FavoriteEntry.CONTENT_URI, values);

        if (uri!=null){
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        }

        finish();
    }
    private class ReviewAsyncTask extends AsyncTask<URL,Void,Reviews>{
        @Override
        protected Reviews doInBackground(URL... urls) {
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

        @Override
        protected void onPostExecute(Reviews movieReviewString) {
            super.onPostExecute(movieReviewString);
            if (movieReviewString!=null) {
                mMovieReviewTextView.setText(movieReviewString.getContents());
                mMovieReviewAuthorTextView.setText(movieReviewString.getAuthor());
            }
        }
    }

    private class TrailerAsyncTask extends AsyncTask<URL,Void,Trailer>{
        @Override
        protected Trailer doInBackground(URL... urls) {
            URL trailerUrl = makeTrailerSearchQuery();
            try {
                String urlResponse = NetworkUtils.getResponseFromHttpUrl(trailerUrl);
                Trailer trailer = JsonUtils.getTrailerContentFromResponse(urlResponse);
                return trailer;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Trailer trailer) {
            super.onPostExecute(trailer);

                //TODO:find way to only play trailers or only play clips , possible trailer.getType();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +trailer.getKey()));

                startActivity(intent);


        }
    }
    private URL makeMovieSearchQuery(){
        URL movieQueryUrl = NetworkUtils.buildReviewUrl(mMovieId);

        return movieQueryUrl;

    }
    private URL makeTrailerSearchQuery(){
        URL movieTrailerQueryUrl = NetworkUtils.buildTrailerUrl(mMovieId);
        return movieTrailerQueryUrl;
    }
}
