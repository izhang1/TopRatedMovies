package app.izhang.topratedmovies;

/*
  Created by ivanzhang on 9/8/17.

  - DetailActivity showing additional data from the movie attributes
 */

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.izhang.topratedmovies.data.Movie;
import app.izhang.topratedmovies.data.Trailer;
import app.izhang.topratedmovies.utilities.MovieLoader;
import app.izhang.topratedmovies.utilities.NetworkUtils;
import app.izhang.topratedmovies.utilities.PosterPathUtils;
import app.izhang.topratedmovies.utilities.TrailerLoader;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Trailer>> {

    private Movie mMovieData;
    private ArrayList<Trailer> mTrailerList;
    private RecyclerView mTrailerRecyclerView;
    private TrailerViewAdapter mTrailerViewAdapter;


    private final static int TRAILER_LOADER_ID = 1002;

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

        // Setup Loader to pull in data
        getLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);

    }


    /**
     * initializes the UI views and populates them with the passed data from mMovieData
     *
     */
    private void showUI(){
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(layoutManager);
        mTrailerRecyclerView.setAdapter(mTrailerViewAdapter);

    }

    /**
     * Populates the movie data, called once the loader is finished or if the activity lifecycle resets
     */
    private void populateTrailerDate(){
        mTrailerViewAdapter.setData(mTrailerList);
        mTrailerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
         /* Checks to see if a value was passed in and passes that along to the loader method */
        TrailerLoader trailerloader = new TrailerLoader(getApplicationContext(), NetworkUtils.TRAILERS, mMovieData.getId());
        return trailerloader;
    }

    @Override
    public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
        mTrailerList = (ArrayList) data;
        populateTrailerDate();
    }

    @Override
    public void onLoaderReset(Loader<List<Trailer>> loader) {
        if(mTrailerList == null){
            getLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);
        }
    }
}
