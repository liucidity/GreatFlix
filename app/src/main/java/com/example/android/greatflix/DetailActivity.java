package com.example.android.greatflix;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.UserDictionary;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.greatflix.data.MovieFavoriteContract;
import com.example.android.greatflix.data.MovieFavoriteDbHelper;
import com.example.android.greatflix.data.MovieFavoriteProvider;
import com.example.android.greatflix.objects.Reviews;
import com.example.android.greatflix.objects.Trailer;
import com.example.android.greatflix.utilities.JsonUtils;
import com.example.android.greatflix.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static android.R.attr.button;
import static android.R.attr.start;
import static com.example.android.greatflix.data.MovieFavoriteContract.FavoriteEntry.TABLE_NAME;
import static java.security.AccessController.getContext;

public class DetailActivity extends AppCompatActivity {

    private String mPosterPath;
    private String mMovieTitle;
    private String mMovieId;
    private Reviews mMovieReview;
    private MovieFavoriteDbHelper mDb;
    private Intent goToFavoriteActivityIntent;


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
        goToFavoriteActivityIntent = new Intent(this, Favorites.class);





        final Intent intentThatStartedTheActivity = getIntent();

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
                Log.d("Details", "Movie Title" + mMovieTitle.toString());
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
            }if (intentThatStartedTheActivity.hasExtra("isAlreadyFavorite")){
                Button button  = (Button) findViewById(R.id.btn_mark_as_favorite);
                final Intent parentIntent = new Intent(this,Favorites.class);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String where = "movie_id = ?";
                        String whereArgs[] = {mMovieId};
                     getContentResolver().delete(MovieFavoriteContract.FavoriteEntry.CONTENT_URI, where, whereArgs);
                        //startActivity(parentIntent);
                    }

                });
            }


        }

    }

    public void addToFavorites(View v) throws SQLiteConstraintException{

        try {

            ContentValues values = new ContentValues();

            values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_NAME, mMovieTitle);
            values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, mMovieId);
            values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING, mReviewScore);
            values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_RELEASE, mReleaseDate);
            values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_POSTER_ID, mPosterPath);
            values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW, mOverview);

            Uri uri = getContentResolver().insert(MovieFavoriteContract.FavoriteEntry.CONTENT_URI, values);
            Log.d("detail", "addToFavorites: " + uri);

            if (uri != null) {
                Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"movie is already in your favorites",Toast.LENGTH_SHORT).show();
        }

    }
    private class ReviewAsyncTask extends AsyncTask<URL,Void,Reviews>{
        @Override
        protected Reviews doInBackground(URL... urls) {
            URL ReviewUrl = makeMovieSearchQuery();
            try {
                String UrlResponse = NetworkUtils.getResponseFromHttpUrl(ReviewUrl);
                mMovieReview = JsonUtils.getReviewContentFromResponse(UrlResponse);


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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent changeActivity = new Intent(this,Favorites.class);
                startActivity(changeActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
