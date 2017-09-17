package app.izhang.topratedmovies.data;

import java.io.Serializable;

/**
 * Created by ivanzhang on 9/8/17.
 * data
 * - model for the movie data that will be pulled in and created from the JSON data.
 */

public class Movie implements Serializable {
    private String title;
    private String release_date;
    private String poster_path;
    private String overview;
    private int vote_average;

    public Movie(String title, String release_date, String poster_path, String overview, int vote_average){
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getVote_average() {
        return vote_average;
    }
}
