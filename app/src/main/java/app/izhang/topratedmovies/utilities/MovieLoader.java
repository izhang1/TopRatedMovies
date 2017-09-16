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
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String TAG = MovieLoader.class.toString();
    private static Context mContext;
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
        Log.v(TAG, "loadInBackground(), mSort: " + mSort);

        URL queryUrl = NetworkUtils.buildUrl(mSort);

        Log.v(TAG, "Query URL: " + queryUrl.toString());
        try{
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            Log.v(TAG, jsonResponse);

            List<Movie> parsedMovieList = MovieJsonUtils.getMovieListFromJson(mContext, jsonResponse);
            return parsedMovieList;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
