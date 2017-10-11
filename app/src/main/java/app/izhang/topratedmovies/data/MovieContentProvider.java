package app.izhang.topratedmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ivanzhang on 10/3/17.
 */

public class MovieContentProvider extends ContentProvider {


    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    private static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIE);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_ID);

        return uriMatcher;
    }

    // Member variable that's initialized in the onCreate method
    private MovieDbHelper mMovieDbHelper;

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    /**
     *  Called when we are querying for all favorite movies on the home page
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        // Match the URI to a specified movie or the whole database
        int match = sUriMatcher.match(uri);
        switch(match){
            case MOVIE:
                // Query the whole DB
                return db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

            case MOVIE_ID:
                // Get the specified ID passed in from the URI
                String movieId = uri.getPathSegments().get(1);

                // Query with the selection and selection args for this specific movie id
                return db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        "movie_id=?",
                        new String[]{movieId},
                        null,
                        null,
                        sortOrder);

        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /**
     *  Called when we're adding a new favorite movie into the database
     *
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        Uri returnUri; // URI to be returned

        long insertReturnId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        if(insertReturnId > 0){
            // This means that there's value inserted
            returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, insertReturnId);
        }else{
            return null;
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    /**
     *  Called when we want to remove a movie from the favorites database
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        int deleteRetValue = 0;

        // Currently only matching a specific movie id
        switch(match){
            case MOVIE:
                return 0;
            case MOVIE_ID:
                // Get the specified ID passed in from the URI
                String movieId = uri.getPathSegments().get(1);
                // Call delete and return the returned value, rows deleted, back to the calling method
                deleteRetValue = db.delete(MovieContract.MovieEntry.TABLE_NAME, "movie_id=?", new String[]{movieId});
        }

        return deleteRetValue;

    }


    /**
     * Movie app will likely not use this call. No items are being updated at this time.
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
