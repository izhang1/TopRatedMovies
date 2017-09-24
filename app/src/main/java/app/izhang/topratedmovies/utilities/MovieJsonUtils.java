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
import app.izhang.topratedmovies.data.Trailer;

/**
 * Created by ivanzhang on 9/9/17.
 * utilities
 * - MovieJsonUtils class
 * - This class contains methods to parse JSON objects and pull out needed information for movies.
 *
 */

class MovieJsonUtils {

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
        final String MOVIE_ID = "id";

        /* String array to hold each day's weather String */
        ArrayList<Movie> movieList = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(MOVIEDB_RESULT);

        /* Looping through the array and saving the movie values into a list of movies passed back to the calling method. */
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject tempObj = (JSONObject) movieArray.get(i);
            Log.v("MovieJsonUtils", tempObj.toString());
            Movie movie = new Movie(tempObj.getString(MOVIE_TITLE),
                    tempObj.getString(MOVIE_RELEASE),
                    tempObj.getString(POSTER_PATH),
                    tempObj.getString(MOVIE_OVERVIEW),
                    tempObj.getInt(VOTE_AVG),
                    tempObj.getString(MOVIE_ID));
            movieList.add(movie);
        }

        return movieList;
    }

    public static List<Trailer> getTrailerFromJson(String trailerJsonStr) throws JSONException{

        final String TRAILER_YOUTUBE = "youtube";

        final String TRAILER_NAME = "name";
        final String TRAILER_SOURCE = "source";

        ArrayList<Trailer> trailerList = new ArrayList<>();

        JSONObject trailerJson = new JSONObject(trailerJsonStr);
        JSONArray trailierArray = trailerJson.getJSONArray(TRAILER_YOUTUBE);

        for(int i = 0; i < trailierArray.length(); i++){
            JSONObject tempObj = (JSONObject) trailierArray.get(i);
            Trailer trailer = new Trailer(tempObj.getString(TRAILER_NAME),
                    tempObj.getString(TRAILER_SOURCE));

            Log.v("Trailer", "Name: " + trailer.getName());
            Log.v("Trailer", "Source: " + trailer.getSource());

            trailerList.add(trailer);
        }

        return trailerList;
    }
}
