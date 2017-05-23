package com.example.android.greatflix;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.android.greatflix.objects.Movies;
import com.example.android.greatflix.utilities.JsonUtils;
import com.example.android.greatflix.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*TODO
TODO: Image adapter
TODO: picasso
TODO: API call
TODO: JSONutils
TODO: networkutils
TODO: Details Screen
TODO: load in background

//todo: change to recyclerview




 */

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    public String CURRENT_QUERY;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    ArrayList<Movies> moviesList;

    private static final String POPULAR_QUERY =  "popular";
    private static final String TOP_RATED_QUERY = "top_rated";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,GridLayoutManager.DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData();
    }

    public void setList(ArrayList<Movies> list) {
        this.moviesList = list;
    }
    private void loadMovieData(){
        URL movieurl = makeMovieSearchQuery();

        new MovieQueryTask().execute(movieurl);
    }
    private URL makeMovieSearchQuery(){
        URL movieQueryUrl = NetworkUtils.buildUrl(CURRENT_QUERY);

        return movieQueryUrl;
        //todo:when query is performed grab images and display them

    }

    @Override
    public void onClick(String currentMovie) {

    }

    public class MovieQueryTask extends AsyncTask<URL,Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Movies> doInBackground(URL... urls) {

            URL movieRequestUrl = makeMovieSearchQuery();
            String QueryResults = null;
            try{
                QueryResults = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                List<Movies> jsonMovieData = JsonUtils.getMoviesFromResponse(QueryResults);
                return jsonMovieData;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }


        protected void onPostExecute(String[] s) {
            if(s!=null) {
                mMovieAdapter.setMovieData(s);
            }
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
            makeMovieSearchQuery();
        }
        if(id == R.id.action_choose_top_rated){
        CURRENT_QUERY = TOP_RATED_QUERY;
            makeMovieSearchQuery();
        }

        return super.onOptionsItemSelected(item);
    }
}
