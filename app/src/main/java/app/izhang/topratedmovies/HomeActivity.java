package app.izhang.topratedmovies;

/*
  Created by ivanzhang on 9/8/17.

  - HomeActivity shows the queries data of movie posters and allows users to navigate to them
  - Also provides the ability for the user to toggle between the sorting of top rated or most popular
 */

import android.content.AsyncTaskLoader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import app.izhang.topratedmovies.data.MovieContract;
import app.izhang.topratedmovies.data.Trailer;
import app.izhang.topratedmovies.utilities.NetworkUtils;
import app.izhang.topratedmovies.utilities.MovieLoader;
import app.izhang.topratedmovies.utilities.TrailerLoader;


public class HomeActivity extends AppCompatActivity{
    private ProgressBar mProgressBar;

    private RecyclerView mMovieRecyclerView;
    private MovieViewAdapter mMovieAdapter;
    private List<Movie> mData;
    private List<Movie> mFavData;

    // Setup variables to save the selected state
    private final static String SELECTED_SORT = "selected";
    private int mSavedSort = -1;

    // Loader to load HTTP results such as the top rated or most popular videos
    private final static int HTTP_LOADER_ID = 1001;

    // Loader to load the internal DB results
    private final static int FAV_DB_LOADER_ID = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Check if the bundle is null or if there's a preexisting instance we need to reinit the activity with
        if(savedInstanceState != null){
            mSavedSort = savedInstanceState.getInt(SELECTED_SORT);
        }

        if(mMovieRecyclerView == null) showUI();

        loadMoviesFromSavedInstance();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_SORT, mSavedSort);
        super.onSaveInstanceState(outState);
    }

    private void loadMoviesFromSavedInstance(){
        Bundle bundle = new Bundle();

        switch (mSavedSort) {

            case R.id.action_favorite:
                Log.v("Loader", "Restarted favorites loader");
                getLoaderManager().restartLoader(FAV_DB_LOADER_ID, null, favoriteLoaderManager);
                changeTitle(getString(R.string.favorite_label));
                break;

            case R.id.action_top_rated:
                Log.v("Loader", "Restarted top rated loader");
                bundle.putInt(getString(R.string.menu_key), NetworkUtils.TOP_RATED);
                getLoaderManager().restartLoader(HTTP_LOADER_ID, bundle, httpSortLoaderManager);
                changeTitle(getString(R.string.top_rated_label));
                break;

            default:
                Log.v("Loader", "Restarted default loader");
                bundle.putInt(getString(R.string.menu_key), NetworkUtils.MOST_POPULAR);
                getLoaderManager().restartLoader(HTTP_LOADER_ID, bundle, httpSortLoaderManager);
                changeTitle(getString(R.string.most_popular_label));
        }
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

    private void changeTitle(String currentSort){
        setTitle(currentSort);
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
        Log.v("Load Movies", "Load movies from saved instance called");

        loadMoviesFromSavedInstance();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.v("On Item selected", "Item Selected");

        int itemId = item.getItemId();

        mSavedSort = itemId;
        loadMoviesFromSavedInstance();

        return true;
    }

    // Loader to load the favorite movies stored in our SQLLite DB
    private LoaderManager.LoaderCallbacks<List<Movie>> favoriteLoaderManager = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
             /* Shows progress bar and hides the recycler view */
            mProgressBar.setVisibility(View.VISIBLE);
            mMovieRecyclerView.setVisibility(View.INVISIBLE);

            return new AsyncTaskLoader<List<Movie>>(getApplicationContext()) {
                // Initialize a Cursor, this will hold all the task data

                // onStartLoading() is called when a loader first starts loading data
                @Override
                protected void onStartLoading() {
                    if (mFavData != null) {
                        // Delivers any previously loaded data immediately
                        deliverResult(mFavData);
                    } else {
                        // Force a new load
                        forceLoad();
                    }
                }

                // loadInBackground() performs asynchronous loading of data
                @Override
                public List<Movie> loadInBackground() {
                    // Will implement to load data

                    Log.v("loadInBackground", "Fav Loader");
                    // Query and load all task data in the background; sort by priority
                    // [Hint] use a try/catch block to catch any errors in loading data

                    try {
                        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);

                        if(cursor != null){
                            mFavData = new ArrayList<>();

                            int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                            int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                            int posterPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
                            int overviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
                            int voteAverageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                            int movieIdIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);

                            while(cursor.moveToNext()){

                                Movie movie = new Movie(cursor.getString(titleIndex),
                                        cursor.getString(releaseDateIndex),
                                        cursor.getString(posterPathIndex),
                                        cursor.getString(overviewIndex),
                                        cursor.getInt(voteAverageIndex),
                                        cursor.getString(movieIdIndex));

                                mFavData.add(movie);

                                //Log.v("Iterative Cursor", movie.toString());

                            }

                        }
                        cursor.close();

                    } catch (Exception e) {
                        Log.e("HomeActivityException", "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;
                    }

                    return mFavData;
                }

            };
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            Log.v("Loader Finished", "Fav Loader");

            /* Shows recycler view and hides the data*/
            mProgressBar.setVisibility(View.INVISIBLE);
            mMovieRecyclerView.setVisibility(View.VISIBLE);

            mData = data;
            populateData();
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            Log.v("Loader Reset", "Fav Loader");
            if(mFavData != null) {
                mData = mFavData;
                populateData();
            }else{
                getLoaderManager().restartLoader(FAV_DB_LOADER_ID, null, favoriteLoaderManager);
            }
        }
    };

    private LoaderManager.LoaderCallbacks<List<Movie>> httpSortLoaderManager = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

            Log.v("HTTP sort", "Loader Created");

            /* Shows progress bar and hides the recycler view */
            mProgressBar.setVisibility(View.VISIBLE);
            mMovieRecyclerView.setVisibility(View.INVISIBLE);

           /* Checks to see if a value was passed in and passes that along to the loader method */
            int passedSort = 0;
            if(args != null) passedSort = args.getInt(getString(R.string.menu_key));
            MovieLoader mMovieLoader = new MovieLoader(getApplicationContext(), passedSort);
            return mMovieLoader;
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

            Log.v("HTTP sort", "Loader Finished");

            /* Shows recycler view and hides the data*/
            mProgressBar.setVisibility(View.INVISIBLE);
            mMovieRecyclerView.setVisibility(View.VISIBLE);

            mData = data;
            populateData();
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            /* Checks to see if data is already available, if not inits the loader */
            Log.v("HTTP sort", "Loader Reset");

            if(mData != null) populateData();
            else getLoaderManager().restartLoader(HTTP_LOADER_ID, null, this);
        }
    };

}
