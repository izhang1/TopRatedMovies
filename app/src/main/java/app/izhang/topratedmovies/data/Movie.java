package app.izhang.topratedmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ivanzhang on 9/8/17.
 * data
 * - model for the movie data that will be pulled in and created from the JSON data.
 */

public class Movie implements Parcelable {
    private String title;
    private String releaseDate;
    private String posterPath;
    private String overview;
    private int voteAverage;
    private String id;

    public Movie(String title, String release_date, String posterPath, String overview, int voteAverage, String id){
        this.title = title;
        this.releaseDate = release_date;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.id = id;
    }

    private Movie(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        voteAverage = in.readInt();
        id = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return releaseDate;
    }

    public void setRelease_date(String release_date) {
        this.releaseDate = release_date;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeInt(voteAverage);
        dest.writeString(id);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie: Title: " + title + "\n"
                + "Release Date: " + releaseDate + "\n"
                + "Poster Path: " + posterPath + "\n"
                + "Overview: " + overview + "\n"
                + "Movie id: " + id;
    }
}
