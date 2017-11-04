package com.example.android.greatflix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.greatflix.objects.Movies;
import com.example.android.greatflix.utilities.JsonUtils;
import com.example.android.greatflix.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private String CURRENT_QUERY;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mProgressBar;
    private TextView mInternetConnectivityTextView;
    private Parcelable layoutManagerSavedState;

    private static final String SAVED_LAYOUT_MANAGER = "savedlayoutmanagerkey";


    private static final String POPULAR_QUERY =  "popular";
    private static final String TOP_RATED_QUERY = "top_rated";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInternetConnectivityTextView = (TextView) findViewById(R.id.tv_no_internet_connection);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_image_list);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);

        CURRENT_QUERY = POPULAR_QUERY;

       loadMovieData();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_LAYOUT_MANAGER, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState instanceof Bundle){
            layoutManagerSavedState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
        }

        super.onRestoreInstanceState(savedInstanceState);

    }

    private void loadMovieData() {

        if (!isNetworkAvailable()) {
            mInternetConnectivityTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mInternetConnectivityTextView.setVisibility(View.INVISIBLE);
            URL movieurl = makeMovieSearchQuery();
            new MovieQueryTask().execute(movieurl);
        }
    }
    private URL makeMovieSearchQuery(){
        URL movieQueryUrl = NetworkUtils.buildUrl(CURRENT_QUERY);

        return movieQueryUrl;
        //todo:when query is performed grab images and display them

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(String currentMoviePoster, String currentMovieTitle, String currentMovieId, String currentMovieReleaseDate, String currentMovieRatings, String currentMovieOverview) {
        Context context = this;


        Class destinationClass = DetailActivity.class;
        Intent startDetailActivityIntent = new Intent(context, destinationClass);
        startDetailActivityIntent.putExtra("posterPath",currentMoviePoster);
        startDetailActivityIntent.putExtra("movieTitle", currentMovieTitle);
        startDetailActivityIntent.putExtra("movieId",currentMovieId);
        startDetailActivityIntent.putExtra("movieReleaseDate", currentMovieReleaseDate);
        startDetailActivityIntent.putExtra("movieRating", currentMovieRatings);
        startDetailActivityIntent.putExtra("movieOverview", currentMovieOverview);
        startActivity(startDetailActivityIntent);

    }

    private class MovieQueryTask extends AsyncTask<URL,Void, List<Movies>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);

        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Movies> doInBackground(URL... urls) {


            URL movieRequestUrl = makeMovieSearchQuery();

            try{
                String QueryResults = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                List<Movies> jsonMovieData = JsonUtils.getMoviesDetailsFromResponse(QueryResults);
                return jsonMovieData;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }



        protected void onPostExecute(List<Movies> s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(s!=null) {

                mMovieAdapter.setMovieData(s);
                restoreLayoutManagerPosition();



            }


        }


    }
    private void restoreLayoutManagerPosition() {
        if (layoutManagerSavedState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.movie, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_choose_popular){
        CURRENT_QUERY = POPULAR_QUERY;
            loadMovieData();
        }
        if(id == R.id.action_choose_top_rated){
        CURRENT_QUERY = TOP_RATED_QUERY;
            loadMovieData();
        }
        if(id == R.id.action_go_to_favorites){
            Intent FavoriteActivityIntent = new Intent(this,Favorites.class);
            startActivity(FavoriteActivityIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
