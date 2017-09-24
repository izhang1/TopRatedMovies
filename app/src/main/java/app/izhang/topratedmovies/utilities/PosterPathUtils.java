package app.izhang.topratedmovies.utilities;

import android.net.Uri;

/**
 * Created by ivanzhang on 9/9/17.
 * utilities
 * - PosterPathUtils class
 * - Utility class that will help create the URL
 * - Moving to create a more dynamic approach based on the size of the screen.
 */

public class PosterPathUtils {

    private static String BASE_URL = "http://image.tmdb.org/t/p/";
    private static String YT_BASE_URL = "https://www.youtube.com/watch?v=";

    // Different Size Params
    private static String SIZE_W92 = "w92";
    private static String SIZE_W154 = "w154";
    private static String SIZE_W185 = "w185";
    private static String SIZE_W342 = "w342";
    private static String SIZE_W500 = "w500";
    private static String SIZE_W780 = "w780";
    private static String SIZE_ORIGINAL = "original";


    /**
     * buildPosterUrl method
     * - Takes in a path and size, returns a built string that can be queried to retrieve the poster image
     * @param path
     * @param size
     * @return
     */
    public static String buildPosterURL(String path, String size){
        // Defaulting size to W500
        return (BASE_URL + SIZE_W500 + "/" + path);
    }

}
