package app.izhang.topratedmovies.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import app.izhang.topratedmovies.data.Movie;

/**
 * Created by ivanzhang on 9/9/17.
 */

public class MovieJsonUtils {

    private static String TAG = "MovieJsonUtils.class";

    public static List<Movie> getMovieListFromJson(Context context, String movieJsonStr)
            throws JSONException {

        /* Results list */
        final String MOVIEDB_RESULT = "results";

        /* All attributes of the movie and their information */
        final String MOVIE_TITLE = "title";
        final String MOVIE_RELEASE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String VOTE_AVG = "vote_average";


        /* String array to hold each day's weather String */
        ArrayList<Movie> movieList = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieJsonStr);
        Log.v(TAG, "movieJson: " + movieJson.toString());

        JSONArray movieArray = movieJson.getJSONArray(MOVIEDB_RESULT);
        Log.v(TAG, "movieArr: " + movieArray.toString());

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject tempObj = (JSONObject) movieArray.get(i);
            Movie movie = new Movie(tempObj.getString(MOVIE_TITLE), tempObj.getString(MOVIE_RELEASE), tempObj.getString(POSTER_PATH), tempObj.getString(MOVIE_OVERVIEW), tempObj.getInt(VOTE_AVG));
            movieList.add(movie);
            Log.v(TAG, "Object" + i + " :"+  tempObj.toString());
        }

        return movieList;
    }
}
