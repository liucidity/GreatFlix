package com.example.android.greatflix;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.greatflix.data.MovieFavoriteContract;
import com.example.android.greatflix.data.MovieFavoriteDbHelper;
import com.example.android.greatflix.objects.Movies;
import com.example.android.greatflix.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.id;

public class Favorites extends AppCompatActivity implements FavoriteAdapter.FavoriteAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView mRecyclerView;
    private static final int URL_LOADER = 0;
    private FavoriteAdapter mAdapter;
    ImageView mImageView;
    List<Movies> favoriteMoviesArrayList = new ArrayList<>();

    private static final String[] PROJECTION = {
            MovieFavoriteContract.FavoriteEntry._ID,
            MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_NAME,
            MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID,
            MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_RELEASE,
            MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING,
            MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW,
            MovieFavoriteContract.FavoriteEntry.COLUMN_POSTER_ID,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mAdapter = new FavoriteAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_favorite_movies);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);



        getSupportLoaderManager().initLoader(URL_LOADER,null,this);
    }


    @Override
    public void onClick(String currentMoviePoster, String currentMovieTitle, String currentMovieId, String currentMovieReleaseDate, String currentMovieRatings, String currentMovieOverview, boolean currentMovieFavoriteStatus) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent startDetailActivityIntent = new Intent(context, destinationClass);
        startDetailActivityIntent.putExtra("posterPath",currentMoviePoster);
        startDetailActivityIntent.putExtra("movieTitle", currentMovieTitle);
        startDetailActivityIntent.putExtra("movieId",currentMovieId);
        startDetailActivityIntent.putExtra("movieReleaseDate", currentMovieReleaseDate);
        startDetailActivityIntent.putExtra("movieRating", currentMovieRatings);
        startDetailActivityIntent.putExtra("movieOverview", currentMovieOverview);
        startDetailActivityIntent.putExtra("isAlreadyFavorite", currentMovieFavoriteStatus);
        startActivity(startDetailActivityIntent);
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Loader<Cursor> loader = null;

        switch (id){
            case URL_LOADER:
                loader = new CursorLoader(this,
                        MovieFavoriteContract.FavoriteEntry.CONTENT_URI,PROJECTION,
                        null,
                        null,
                        "_id ASC");
            break;

        }

        return loader;
    }
//todo: set these to text / image views
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while(data.moveToNext()){
            int index;
            index = data.getColumnIndexOrThrow(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_NAME);
            String movie_title = data.getString(index);

            index = data.getColumnIndexOrThrow(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING);
            String rating = data.getString(index);

            index = data.getColumnIndexOrThrow(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_RELEASE);
            String release_date = data.getString(index);

            index = data.getColumnIndexOrThrow(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID);
            String movie_id = data.getString(index);

            index = data.getColumnIndexOrThrow(MovieFavoriteContract.FavoriteEntry.COLUMN_POSTER_ID);
            String movie_poster = data.getString(index);

            index = data.getColumnIndexOrThrow(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW);
            String overview = data.getString(index);


            Movies moviesWithPath = new Movies(movie_poster,movie_title,movie_id,release_date,rating,overview);
            favoriteMoviesArrayList.add(moviesWithPath);
            Log.d("Favorites", "onLoadFinished: there are " + favoriteMoviesArrayList.size() + " favorite movies");


            if (favoriteMoviesArrayList != null) {
                mAdapter.setFavoritesData(favoriteMoviesArrayList);

            }
            if (data != null) {
                TextView noFavoritesTextView = (TextView) findViewById(R.id.tv_no_favorites_added_message);
                noFavoritesTextView.setVisibility(View.INVISIBLE);
            }


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { //doesnt seem to be called
        mAdapter.clear();
        Log.d("favo", "onLoaderReset: adapter cleared   ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSupportLoaderManager().restartLoader(URL_LOADER,null,this);

    }
}
