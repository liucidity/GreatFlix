package com.example.android.greatflix.objects;

import static android.R.attr.id;

/**
 * Created by TRAVIS on 2017-05-21.
 */

public class Movies implements Cloneable{

    private String posterPath;
    private String releaseDate;
    private String title;
    private String rating;
    private String overview;





    public Movies(String posterPath, String title, String releaseDate, String rating, String overview) {
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.rating = rating;
        this.overview = overview;
    }

    public Movies(String posterPath) {

        this.posterPath = posterPath;

    }


    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public String getOverview() {
        return overview;
    }
}
