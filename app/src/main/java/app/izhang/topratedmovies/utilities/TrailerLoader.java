package app.izhang.topratedmovies.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import app.izhang.topratedmovies.data.Trailer;

/**
 * Created by ivanzhang on 9/24/17.
 * utilities
 * - TrailerLoader class
 * - extends AsyncTaskLoader and helps with querying the network request on a separate thread.
 * - calls the appropriate HTTP requests, parses the JSON data and returns a list of Trailers back to the main thread
 *
 */

public class TrailerLoader extends AsyncTaskLoader<List<Trailer>>{

    private int mSort = 0;
    private String mMovieId = "";

    public TrailerLoader(Context context, int passedSort, String movieId) {
        super(context);
        mSort = passedSort;
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {
        /* Builds the URL we'll use to query */
        URL queryUrl = NetworkUtils.buildUrlWithId(mSort, mMovieId);

        /* Queries the URL and parses the data using the MovieJsonUtils method getMovieListFromJson */
        try{
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);

            return MovieJsonUtils.getTrailerFromJson(jsonResponse);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
