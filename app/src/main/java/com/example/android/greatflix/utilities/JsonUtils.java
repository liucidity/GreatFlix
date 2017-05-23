package com.example.android.greatflix.utilities;

import com.example.android.greatflix.objects.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

        Movies movieWithPath;
        movieWithPath = null;
        movieWithPath.setPosterPath(NetworkUtils.buildMoviePosterPath(posterPath).toString());
        moviesArrayList.add(movieWithPath);
        //todo: test this
    }
    return moviesArrayList;
    }
}
