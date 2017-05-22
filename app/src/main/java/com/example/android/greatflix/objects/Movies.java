package com.example.android.greatflix.objects;

/**
 * Created by TRAVIS on 2017-05-21.
 */

public class Movies {
    private int id;
    private String posterPath;
    private String title;

    public Movies(int id, String posterPath, String title) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
    }
    public Movies(String posterPath) {

        this.posterPath = posterPath;

    }

    public int getId() {
        return id;
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
}
