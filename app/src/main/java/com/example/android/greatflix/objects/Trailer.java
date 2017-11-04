package com.example.android.greatflix.objects;

/**
 * Created by TRAVIS on 2017-09-06.
 */

public class Trailer {
    private String key;
    private String type;

    public Trailer(String key) {
        this.key = key;
    }

    public Trailer(String key, String type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

}
