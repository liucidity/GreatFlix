package com.example.android.greatflix;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.android.greatflix.objects.Reviews;
import com.example.android.greatflix.objects.Trailer;
import com.example.android.greatflix.utilities.JsonUtils;
import com.example.android.greatflix.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

//will need to implement RecyclerView for Trailers and user reviews

public class DetailActivity extends AppCompatActivity {

    private String mPosterPath;
    private String mMovieTitle;
    private String mMovieId;
    private Reviews mMovieReview;
    private String mReleaseDate;
    private String mReviewScore;
    private String mOverview;




    @BindView(R.id.tv_review)
    TextView mMovieReviewTextView;
    @BindView(R.id.tv_review_author)
    TextView mMovieReviewAuthorTextView;
    @BindView(R.id.tv_movie_title)
    TextView mMovieTitleTextView;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;
    @BindView(R.id.tv_overview)
    TextView mOverviewTextView;
    @BindView(R.id.tv_ratings)
    TextView mRatingTextView;



    @BindView(R.id.iv_detail)
    ImageView mImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        final Intent intentThatStartedTheActivity = getIntent();

        if(intentThatStartedTheActivity!=null){
            if(intentThatStartedTheActivity.hasExtra(getString(R.string.poster_path_extra))){
                mPosterPath = intentThatStartedTheActivity.getStringExtra(getString(R.string.poster_path_extra));


                String MoviePosterPath = mPosterPath;

                Uri uri = Uri.parse(NetworkUtils.buildMoviePosterPath(MoviePosterPath).toString());
                ImageView ivBasicImage = mImageView;
                Context context = ivBasicImage.getContext();
                Picasso.with(context).load(uri).resize(342,514).centerInside().into(ivBasicImage);

            }if (intentThatStartedTheActivity.hasExtra(getString(R.string.movie_title_extra))){
                mMovieTitle = intentThatStartedTheActivity.getStringExtra(getString(R.string.movie_title_extra));
                mMovieTitleTextView.setText(mMovieTitle);
                Log.d("Details", "Movie Title" + mMovieTitle.toString());
            }
            if (intentThatStartedTheActivity.hasExtra(getString(R.string.movie_id_extra))){
                mMovieId = intentThatStartedTheActivity.getStringExtra(getString(R.string.movie_id_extra));

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
            if (intentThatStartedTheActivity.hasExtra(getString(R.string.movie_release_date_extra))){
                mReleaseDate = intentThatStartedTheActivity.getStringExtra(getString(R.string.movie_release_date_extra));
                mReleaseDateTextView.setText(mReleaseDate);
            }if (intentThatStartedTheActivity.hasExtra(getString(R.string.movie_rating_extra))){
                mReviewScore = intentThatStartedTheActivity.getStringExtra(getString(R.string.movie_rating_extra));
                mRatingTextView.setText(mReviewScore);
            }if (intentThatStartedTheActivity.hasExtra(getString(R.string.movie_overview_extra))){
                mOverview = intentThatStartedTheActivity.getStringExtra(getString(R.string.movie_overview_extra));
                mOverviewTextView.setText(mOverview);
            }if (intentThatStartedTheActivity.hasExtra(getString(R.string.is_already_favorite_extra))){
                Button button  = (Button) findViewById(R.id.btn_mark_as_favorite);
                button.setText(R.string.remove_favorite_button_text);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String where = "movie_id = ?";
                        String whereArgs[] = {mMovieId};
                     getContentResolver().delete(MovieFavoriteContract.FavoriteEntry.CONTENT_URI, where, whereArgs);

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
            Toast.makeText(this, R.string.toast_movie_already_favorite,Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_youtube_video_id) +trailer.getKey()));

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
