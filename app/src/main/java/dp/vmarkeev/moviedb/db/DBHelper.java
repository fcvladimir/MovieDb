package dp.vmarkeev.moviedb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import dp.vmarkeev.moviedb.models.movies.Results;

/**
 * Created by vmarkeev on 01.03.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MovieDb";

    private static final String TABLE_MOVIES = "movies";

    private static final String _ID = "_id";
    private static final String MOVIE_ID = "movie_id";
    private static final String MOVIE_POSTER = "movie_poster";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYLIST_SONG_TABLE = "CREATE TABLE movies (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "movie_id TEXT," +
                "movie_poster TEXT)";
        db.execSQL(CREATE_PLAYLIST_SONG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS movies");
        this.onCreate(db);
    }

    public boolean addMovie(String movieId, String moviePoster) {
        if (isMovieAlreadyInDb(movieId)) {
            return false;
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.putNull(_ID);
            values.put(MOVIE_ID, movieId);
            values.put(MOVIE_POSTER, moviePoster);

            db.insert(TABLE_MOVIES, null, values);
            db.close();
            return true;
        }
    }

    private boolean isMovieAlreadyInDb(String fieldValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MOVIES + " WHERE " + MOVIE_ID + " = " + fieldValue;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<Results> getAllPlaylist() {
        String query = "SELECT * FROM " + TABLE_MOVIES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Results> playlist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                playlist.add(getPlaylistFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        db.close();
        return playlist;
    }

    private Results getPlaylistFromCursor(Cursor cursor) {
        String movieId = cursor.getString(1);
        return new Results(movieId, cursor.getString(2));
    }
}
