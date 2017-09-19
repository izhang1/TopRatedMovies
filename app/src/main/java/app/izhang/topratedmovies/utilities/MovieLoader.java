package app.izhang.topratedmovies.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import app.izhang.topratedmovies.data.Movie;

/**
 * Created by ivanzhang on 9/16/17.
 * utilities
 * - MovieLoader class
 * - extends AsyncTaskLoader and helps with querying the network request on a separate thread.
 * - Loaders also aligns with the activity lifecycle and will adjust to screen flips
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private Context mContext;
    private int mSort = 0;

    public MovieLoader(Context context, int passedSort) {
        super(context);
        mSort = passedSort;
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {

        URL queryUrl = NetworkUtils.buildUrl(mSort);

        try{
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);

            List<Movie> parsedMovieList = MovieJsonUtils.getMovieListFromJson(mContext, jsonResponse);
            return parsedMovieList;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
