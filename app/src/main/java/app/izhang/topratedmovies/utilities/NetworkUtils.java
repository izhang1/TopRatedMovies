package app.izhang.topratedmovies.utilities;

/*
  Created by ivanzhang on 9/8/17.
  utilities
  - NetworkUtils class
  - abstract some of the networking requests being made

 */

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {


    // TODO: Change the API key to your own
    private static final String API_KEY = "<YOUR_KEY>";

    private static final String TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static final String MOST_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";


    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API.If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this course.
     */


    // Passed int that represents top rated
    public static final int TOP_RATED = 1;
    public static final int MOST_POPULAR = 2;


    /**
     * Builds the URL based on the passed in sort the user wants.
     *
     * @param passedCategory The sort that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(int passedCategory) {

        Uri builtUri;

        if(passedCategory == TOP_RATED){
            builtUri = Uri.parse(TOP_RATED_URL);
        }else{
            builtUri = Uri.parse(MOST_POPULAR_URL);
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}