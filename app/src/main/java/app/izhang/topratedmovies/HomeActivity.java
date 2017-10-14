package app.izhang.topratedmovies;

/**
  Created by ivanzhang on 9/8/17.

  - HomeActivity shows the queries data of movie posters and allows users to navigate to them
  - Also provides the ability for the user to toggle between the sorting of top rated or most popular
 */

import android.content.AsyncTaskLoader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Parcelable;
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
import app.izhang.topratedmovies.data.MovieContract;
import app.izhang.topratedmovies.utilities.NetworkUtils;
import app.izhang.topratedmovies.utilities.MovieLoader;


public class HomeActivity extends AppCompatActivity{
    private ProgressBar mProgressBar;

    private RecyclerView mMovieRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MovieViewAdapter mMovieAdapter;
    private List<Movie> mTopRatedData;
    private List<Movie> mMostPopData;
    private List<Movie> mFavData;

    // Setup variables to save the selected state
    private final static String SELECTED_SORT = "selected";
    private int mSavedSort = -1;

    // Loader to load HTTP results such as the top rated or most popular videos
    private final static int HTTP_TOP_RATED_LOADER_ID = 1001;
    private final static int HTTP_MOST_POP_LOADER_ID = 1002;

    // Loader to load the internal DB results
    private final static int FAV_DB_LOADER_ID = 2001;

    public final static String RV_STATE = "rv_state";
    Parcelable rvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(mMovieRecyclerView == null) showUI();

        // Check if the bundle is null or if there's a preexisting instance we need to reinit the activity with
        if(savedInstanceState != null){
            mSavedSort = savedInstanceState.getInt(SELECTED_SORT);
            loadMovies(mSavedSort);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        rvState = mGridLayoutManager.onSaveInstanceState();
        outState.putParcelable(RV_STATE, rvState);
        outState.putInt(SELECTED_SORT, mSavedSort);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            rvState = savedInstanceState.getParcelable(RV_STATE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(rvState != null){
            // Restore the state of the scroll to the current location
            mGridLayoutManager.onRestoreInstanceState(rvState);
        }
    }

    private void loadMovies(int sort){
        Bundle bundle = new Bundle();

        // Checks the saved instance to see if a sort was previously defined
        switch (sort) {
            case R.id.action_favorite:
                getLoaderManager().restartLoader(FAV_DB_LOADER_ID, null, favoriteLoaderManager);
                changeTitle(getString(R.string.favorite_label));
                getLoaderManager().destroyLoader(HTTP_MOST_POP_LOADER_ID);
                getLoaderManager().destroyLoader(HTTP_TOP_RATED_LOADER_ID);
                break;

            case R.id.action_most_popular:
                bundle.putInt(getString(R.string.menu_key), NetworkUtils.MOST_POPULAR);
                getLoaderManager().initLoader(HTTP_MOST_POP_LOADER_ID, bundle, httpMostPopLoaderManager);
                changeTitle(getString(R.string.most_popular_label));
                getLoaderManager().destroyLoader(HTTP_TOP_RATED_LOADER_ID);
                getLoaderManager().destroyLoader(FAV_DB_LOADER_ID);
                break;

            default:
                bundle.putInt(getString(R.string.menu_key), NetworkUtils.TOP_RATED);
                getLoaderManager().initLoader(HTTP_TOP_RATED_LOADER_ID, bundle, httpTopRatedLoaderManager);
                changeTitle(getString(R.string.top_rated_label));
                getLoaderManager().destroyLoader(HTTP_MOST_POP_LOADER_ID);
                getLoaderManager().destroyLoader(FAV_DB_LOADER_ID);
                break;
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

        mGridLayoutManager
                = new GridLayoutManager(this, layoutCountBasedOnOrientation());

        mMovieRecyclerView.setLayoutManager(mGridLayoutManager);

        mMovieRecyclerView.setAdapter(mMovieAdapter);

        // Initial loading of movies
        if(getLoaderManager().getLoader(HTTP_TOP_RATED_LOADER_ID) == null) {
            loadMovies(R.id.action_top_rated);
        }
    }

    private void changeTitle(String currentSort){
        setTitle(currentSort);
    }

    /**
     * Called after Loader finishes. Populates the data into the recycler view.
     *
     */
    private void populateData(List<Movie> data){

        mMovieAdapter.setData((ArrayList<Movie>) data);
        mMovieAdapter.notifyDataSetChanged();

        // Restore the state of the scroll to the current location
        if(rvState != null) mGridLayoutManager.onRestoreInstanceState(rvState);

    }

    /**
     * Called after Loader finishes. Populates the data into the recycler view.
     *
     */
    private void populateFavData(){

        mMovieAdapter.setData((ArrayList<Movie>) mFavData);
        mMovieAdapter.notifyDataSetChanged();

        // Restore the state of the scroll to the current location
        if(rvState != null) mGridLayoutManager.onRestoreInstanceState(rvState);

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
        int itemId = item.getItemId();

        // Saves the sort referencing the specific menuitem
        if(mSavedSort != itemId) {
            // Return the state to the top of the scroll position
            mGridLayoutManager.smoothScrollToPosition(mMovieRecyclerView, null, 0);
            rvState = null;

            mSavedSort = itemId;

            loadMovies(itemId);
        }

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
                    forceLoad();
                }

                // loadInBackground() performs asynchronous loading of data
                @Override
                public List<Movie> loadInBackground() {
                    // Will implement to load data

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

                        if(cursor != null) cursor.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                    return mFavData;
                }

            };
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

            /* Shows recycler view and hides the data*/
            mProgressBar.setVisibility(View.INVISIBLE);
            mMovieRecyclerView.setVisibility(View.VISIBLE);

            mFavData = data;
            populateFavData();
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            //getLoaderManager().restartLoader(FAV_DB_LOADER_ID, null, favoriteLoaderManager);
        }
    };

    // Loaders to call the HTTP requests and retrieve the top rated or popular movies
    private LoaderManager.LoaderCallbacks<List<Movie>> httpTopRatedLoaderManager = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

            /* Shows progress bar and hides the recycler view */
            mProgressBar.setVisibility(View.VISIBLE);
            mMovieRecyclerView.setVisibility(View.INVISIBLE);

           /* Checks to see if a value was passed in and passes that along to the loader method */
            int passedSort = 0;
            if(args != null) passedSort = args.getInt(getString(R.string.menu_key));
            return new MovieLoader(getApplicationContext(), passedSort);
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

            /* Shows recycler view and hides the data*/
            mProgressBar.setVisibility(View.INVISIBLE);
            mMovieRecyclerView.setVisibility(View.VISIBLE);

            mTopRatedData = data;
            populateData(mTopRatedData);
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            //getLoaderManager().restartLoader(HTTP_TOP_RATED_LOADER_ID, null, this);
        }
    };
    private LoaderManager.LoaderCallbacks<List<Movie>> httpMostPopLoaderManager = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

            /* Shows progress bar and hides the recycler view */
            mProgressBar.setVisibility(View.VISIBLE);
            mMovieRecyclerView.setVisibility(View.INVISIBLE);

           /* Checks to see if a value was passed in and passes that along to the loader method */
            int passedSort = 0;
            if(args != null) passedSort = args.getInt(getString(R.string.menu_key));
            return new MovieLoader(getApplicationContext(), passedSort);
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            /* Shows recycler view and hides the data*/
            mProgressBar.setVisibility(View.INVISIBLE);
            mMovieRecyclerView.setVisibility(View.VISIBLE);

            mMostPopData = data;
            populateData(mMostPopData);
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            //getLoaderManager().restartLoader(HTTP_MOST_POP_LOADER_ID, null, this);
        }
    };

}
