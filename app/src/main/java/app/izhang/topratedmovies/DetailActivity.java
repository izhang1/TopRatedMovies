package app.izhang.topratedmovies;

/*
  Created by ivanzhang on 9/8/17.

  - DetailActivity showing additional data from the movie attributes
 */

import android.app.LoaderManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.izhang.topratedmovies.data.Movie;
import app.izhang.topratedmovies.data.MovieContentProvider;
import app.izhang.topratedmovies.data.MovieContract;
import app.izhang.topratedmovies.data.Trailer;
import app.izhang.topratedmovies.utilities.MovieLoader;
import app.izhang.topratedmovies.utilities.NetworkUtils;
import app.izhang.topratedmovies.utilities.PosterPathUtils;
import app.izhang.topratedmovies.utilities.ReviewLoader;
import app.izhang.topratedmovies.utilities.TrailerLoader;

public class DetailActivity extends AppCompatActivity{

    private Movie mMovieData;
    private ContentResolver mContentResolver;

    private ArrayList<Trailer> mTrailerList;
    private RecyclerView mTrailerRecyclerView;
    private TrailerViewAdapter mTrailerViewAdapter;

    private ArrayList<String> mReviewList;
    private ReviewViewAdapter mReviewViewAdapter;
    private RecyclerView mReviewRecyclerView;

    private Button mFavoriteButton;

    private final static int TRAILER_LOADER_ID = 1000;
    private final static int REVIEW_LOADER_ID = 2000;


    private LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoaderListener = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            TrailerLoader trailerloader = new TrailerLoader(getApplicationContext(), NetworkUtils.TRAILERS, mMovieData.getId());
            return trailerloader;
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            mTrailerList = (ArrayList) data;
            populateTrailerData();
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {
            if(mTrailerList == null){
                getLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);
            }
        }
    };

    private LoaderManager.LoaderCallbacks<List<String>> reviewLoaderListener = new LoaderManager.LoaderCallbacks<List<String>>() {
        @Override
        public Loader<List<String>> onCreateLoader(int id, Bundle args) {
            ReviewLoader reviewLoader = new ReviewLoader(getApplicationContext(), NetworkUtils.REVIEWS, mMovieData.getId());
            return reviewLoader;
        }

        @Override
        public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
            mReviewList = (ArrayList) data;
            populateReviewData();
        }

        @Override
        public void onLoaderReset(Loader<List<String>> loader) {
            if(mReviewList == null){
                getLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle receivedData = getIntent().getExtras();
        if(receivedData != null) {
            mMovieData = receivedData.getParcelable(getString(R.string.key_movie_object));
        }

        setTitle("Movie Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showUI();

        // Setup Loader to pull in data, both trailers and reviews
        getLoaderManager().initLoader(TRAILER_LOADER_ID, null, trailerLoaderListener);
        getLoaderManager().initLoader(REVIEW_LOADER_ID, null, reviewLoaderListener);
    }


    /**
     * initializes the UI views and populates them with the passed data from mMovieData
     *
     */
    private void showUI(){

        mFavoriteButton = (Button) findViewById(R.id.btn_favorite);
        if(currentMovieIsFavorite() == true){
            // Set the text to unfavorite. Default is favorite
            mFavoriteButton.setText(getString(R.string.unfavorite_label));
        }

        ImageView mPosterView = (ImageView) findViewById(R.id.poster_imageview);
        String posterPath = PosterPathUtils.buildPosterURL(mMovieData.getPosterPath(), "size");
        Picasso.with(this).load(posterPath).into(mPosterView);

        TextView mVoteAverageTV = (TextView) findViewById(R.id.tv_vote_average);
        TextView mOverviewTV = (TextView) findViewById(R.id.tv_movie_overview);
        TextView mReleaseDateTV = (TextView) findViewById(R.id.tv_release_date);
        TextView mMovieTitleTV = (TextView) findViewById(R.id.tv_movie_title);

        mMovieTitleTV.setText(mMovieData.getTitle() + " ID:" + mMovieData.getId());
        mVoteAverageTV.setText(getString(R.string.vote_average_label) + mMovieData.getVoteAverage());
        mOverviewTV.setText(getString(R.string.overview_label) + mMovieData.getOverview());
        mReleaseDateTV.setText(getString(R.string.overview_label) + mMovieData.getRelease_date());

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mTrailerViewAdapter = new TrailerViewAdapter();

        LinearLayoutManager trailerLayoutMgr = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutMgr);
        mTrailerRecyclerView.setAdapter(mTrailerViewAdapter);

        LinearLayoutManager reviewLayoutMgr = new LinearLayoutManager(this);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);
        mReviewViewAdapter = new ReviewViewAdapter();

        mReviewRecyclerView.setLayoutManager(reviewLayoutMgr);
        mReviewRecyclerView.setAdapter(mReviewViewAdapter);

    }

    /**
     * Queries the database and determine if this specific movie is available or not.
     *
     * @return
     */
    private boolean currentMovieIsFavorite(){
        mContentResolver = this.getContentResolver();

        Uri.Builder builder = MovieContract.MovieEntry.CONTENT_URI
                .buildUpon()
                .appendPath(mMovieData.getId());

        Cursor queriedMovie = mContentResolver.query(builder.build(), null, null, null, null, null);

        // Return false if the content resolve does not return anything
        int count = queriedMovie.getCount();
        Log.v("Cursor", "Count: " + count);

        if(count <= 0){
            return false;
        }else{
            return true;
        }

    }

    /**
     * Populates the movie data, called once the loader is finished or if the activity lifecycle resets
     */
    private void populateTrailerData(){
        mTrailerViewAdapter.setData(mTrailerList);
        mTrailerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Populates the movie data, called once the loader is finished or if the activity lifecycle resets
     */
    private void populateReviewData(){
        mReviewViewAdapter.setData(mReviewList);
        mTrailerViewAdapter.notifyDataSetChanged();
    }

    /**
     *  OnClick - Deletes or Inserts the movie and changes the UI accordingly
     *
     * @param view
     */
    public void onFavoriteButton(View view){

        Uri.Builder builder = MovieContract.MovieEntry.CONTENT_URI
                .buildUpon()
                .appendPath(mMovieData.getId());

        if(mFavoriteButton.getText().equals(getString(R.string.favorite_label))) {
            // Favorite the movie. Put it into the databse.
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovieData.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovieData.getRelease_date());
            values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovieData.getPosterPath());
            values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovieData.getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovieData.getVoteAverage());
            values.put(MovieContract.MovieEntry.COLUMN_ID, mMovieData.getId());

            // mContentResolver.query()
            mContentResolver.insert(builder.build(), values);

            // Change the the text on the button to "unfavorite"
            mFavoriteButton.setText(getString(R.string.unfavorite_label));

        }else{
            // Unfavorite the movie. Remove it from the database.
            mContentResolver.delete(builder.build(), null, null);

            // Change the the text on the button to "favorite"
            mFavoriteButton.setText(getString(R.string.favorite_label));

        }



    }

}
