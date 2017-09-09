package com.example.android.greatflix.objects;

/**
 * Created by TRAVIS on 2017-09-05.
 */

public class Reviews {

    private String author;
    private String contents;
    private String date;

    public Reviews(String author, String contents) {
        this.author = author;
        this.contents = contents;
    }

    public String getAuthor() {
        return author;
    }

    public String getContents() {
        return contents;
    }
}
