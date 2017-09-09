package com.example.android.greatflix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.greatflix.MovieFavoriteContract.FavoriteEntry;

import static android.R.attr.version;

/**
 * Created by TRAVIS on 2017-09-07.
 */

public class MovieFavoriteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 1;

    public MovieFavoriteDbHelper(Context context) {
        super(context,DATABASE_NAME,null , version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        addFavoritesTable(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void addFavoritesTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE "+ FavoriteEntry.TABLE_NAME + " (" +

                FavoriteEntry._ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteEntry.COLUMN_ID             + " INTEGER NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_NAME     + " TEXT NOT NULL, "    +
                FavoriteEntry.COLUMN_MOVIE_RELEASE  + " TEXT NOT NULL, "    +
                FavoriteEntry.COLUMN_MOVIE_RATING   + " TEXT NOT NULL, "    +
                FavoriteEntry.COLUMN_POSTER_ID      + " TEXT NOT NULL"

        );
    }
}
