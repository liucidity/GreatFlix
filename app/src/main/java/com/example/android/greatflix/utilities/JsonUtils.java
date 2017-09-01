package com.example.android.greatflix.utilities;

import com.example.android.greatflix.objects.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by TRAVIS on 2017-05-19.
 */

public final class JsonUtils {


public static List<Movies> getMoviesFromResponse(String urlResponse) throws JSONException {

    List<Movies> moviesArrayList = new ArrayList<>();

    JSONObject jsonObject = new JSONObject(urlResponse);

    JSONArray movies = jsonObject.getJSONArray("results");

    for (int i =0 ; i<movies.length(); i++){
        JSONObject posterPathJSONObject = movies.getJSONObject(i);
        String posterPath = posterPathJSONObject.getString("poster_path");


        Movies movieWithPath = new Movies(posterPath);

        moviesArrayList.add(movieWithPath);
        //todo: test this
    }
    return moviesArrayList;
    }
    public static List<Movies> getMoviesDetailsFromResponse(String urlResponse) throws JSONException {

        List<Movies> moviesArrayList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(urlResponse);

        JSONArray movies = jsonObject.getJSONArray("results");

        for (int i =0 ; i<movies.length(); i++){
            JSONObject JSONObject = movies.getJSONObject(i);
            String posterPath = JSONObject.getString("poster_path");
            String titlePath = JSONObject.getString("original_title");
            String movieIdPath = JSONObject.getString("id");
            String releaseDatePath = JSONObject.getString("release_date");
            String overviewPath = JSONObject.getString("overview");
            String votePath = JSONObject.getString("vote_average");


            Movies movieWithPath = new Movies(posterPath,titlePath,movieIdPath,releaseDatePath,votePath,overviewPath);

            moviesArrayList.add(movieWithPath);

        }
        return moviesArrayList;
    }
    public static String getReviewContentFromResponse(String urlResponse)throws JSONException{
        JSONObject jsonObject = new JSONObject(urlResponse);

        JSONArray reviews = jsonObject.getJSONArray("results");
        JSONObject JSONObject = reviews.getJSONObject(0);

        String contentPath = JSONObject.getString("content");

        return contentPath;
    }



}
