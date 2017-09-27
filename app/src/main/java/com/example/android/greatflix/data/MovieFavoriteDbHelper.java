package com.example.android.greatflix.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.greatflix.data.MovieFavoriteContract.FavoriteEntry;

/**
 * Created by TRAVIS on 2017-09-07.
 */

public class MovieFavoriteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 16844061;

    public MovieFavoriteDbHelper(Context context) {
        super(context,DATABASE_NAME,null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        addFavoritesTable(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
    }

    private void addFavoritesTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE "+ FavoriteEntry.TABLE_NAME + " (" +

                FavoriteEntry._ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteEntry.COLUMN_MOVIE_ID       + " TEXT NOT NULL UNIQUE, " +
                FavoriteEntry.COLUMN_MOVIE_NAME     + " TEXT NOT NULL, "    +
                FavoriteEntry.COLUMN_MOVIE_RELEASE  + " TEXT NOT NULL, "    +
                FavoriteEntry.COLUMN_MOVIE_RATING   + " TEXT NOT NULL, "    +
                FavoriteEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "    +
                FavoriteEntry.COLUMN_POSTER_ID      + " TEXT NOT NULL);"

        );
    }
}
