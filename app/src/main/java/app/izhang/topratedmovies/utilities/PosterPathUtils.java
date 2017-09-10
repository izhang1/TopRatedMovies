package app.izhang.topratedmovies.utilities;

/**
 * Created by ivanzhang on 9/9/17.
 */

public class PosterPathUtils {

    //Then you will need a ‘size’, which will be one of the following:
    // "w92", "w154", "w185", "w342", "w500", "w780", or "original". For most phones we recommend using “w185”.
    // Ex) http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
    private static String BASE_URL = "http://image.tmdb.org/t/p/";

    // Different Size Params
    private static String SIZE_W92 = "w92";
    private static String SIZE_W154 = "w154";
    private static String SIZE_W185 = "w185";
    private static String SIZE_W342 = "w342";
    private static String SIZE_W500 = "w500";
    private static String SIZE_W780 = "w780";
    private static String SIZE_ORIGINAL = "original";

    public static String buildPosterURL(String path, String size){
        // Defaulting size to W154
        return (BASE_URL + SIZE_W500 + "/" + path);
    }
}
