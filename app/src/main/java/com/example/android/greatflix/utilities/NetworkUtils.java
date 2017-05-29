package com.example.android.greatflix.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.greatflix.objects.Movies;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by TRAVIS on 2017-05-19.
 */

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";


    private static final String API_PARAM = "api_key";

    //create your own api key for free at https://www.themoviedb.org
    private static final String API_KEY = "";
    private static final String imageSize = "w185";

    //currently this buildUrl only works for popular movies todo: allow it to select between popular and top rate


    public static URL buildUrl(String QueryType){

        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(QueryType)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);
        return url;
    }
    public static URL buildMoviePosterPath(String posterPath){
        Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(imageSize)
                .appendPath(posterPath.substring(1))
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.v(TAG, "Built Poster URI " + url);
        return url;

    }

    public static String getResponseFromHttpUrl (URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream movieInputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(movieInputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }

    }





}
