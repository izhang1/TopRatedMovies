package app.izhang.topratedmovies;

/*
  Created by ivanzhang on 9/8/17.

  - HomeActivity shows the queries data of movie posters and allows users to navigate to them
  - Also provides the ability for the user to toggle between the sorting of top rated or most popular
 */

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.content.Loader;
import android.app.LoaderManager;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import app.izhang.topratedmovies.data.Movie;
import app.izhang.topratedmovies.utilities.NetworkUtils;
import app.izhang.topratedmovies.utilities.MovieLoader;


public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>{
    private ProgressBar mProgressBar;

    private RecyclerView mMovieRecyclerView;
    private MovieViewAdapter mMovieAdapter;
    private List<Movie> mData;

    private final static int LOADER_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showUI();
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    /**
     * initializes the UI views
     *
     */
    private void showUI(){
        mProgressBar = (ProgressBar) this.findViewById(R.id.loading_progress_bar);
        mMovieRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mMovieAdapter = new MovieViewAdapter();

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this, layoutCountBasedOnOrientation());

        mMovieRecyclerView.setLayoutManager(gridLayoutManager);

        mMovieRecyclerView.setAdapter(mMovieAdapter);
    }

    /**
     * Called after Loader finishes. Populates the data into the recycler view.
     *
     */
    private void populateData(){
        mMovieAdapter.setData((ArrayList<Movie>) mData);
        mMovieAdapter.notifyDataSetChanged();
    }

    /**
     * helper method to determine the orientation of the phone and return the grid length layout
     *
     */
    private int layoutCountBasedOnOrientation(){
        final int TWO_COLUMNS = 2;
        final int FOUR_COLUMNS = 4;

        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
            return TWO_COLUMNS;
        } else {
            return FOUR_COLUMNS;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();

        // Switch case between the most popular and top rated
        // Pass an integer value to identity which sort was chosen
        switch (item.getItemId()){
            case R.id.action_most_popular:
                bundle.putInt(getString(R.string.menu_key), NetworkUtils.MOST_POPULAR);
                getLoaderManager().restartLoader(LOADER_ID, bundle, this);
                break;
            case R.id.action_top_rated:
                bundle.putInt(getString(R.string.menu_key), NetworkUtils.TOP_RATED);
                getLoaderManager().restartLoader(LOADER_ID, bundle, this);
                break;
        }

        return true;
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        mProgressBar.setVisibility(View.VISIBLE);
        mMovieRecyclerView.setVisibility(View.INVISIBLE);

        int passedSort = 0;
        if(args != null) passedSort = args.getInt(getString(R.string.menu_key));
        MovieLoader mMovieLoader = new MovieLoader(getApplicationContext(), passedSort);
        return mMovieLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovieRecyclerView.setVisibility(View.VISIBLE);

        mData = data;
        populateData();
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        if(mData != null) populateData();
        else getLoaderManager().initLoader(LOADER_ID, null, this);
    }

}
