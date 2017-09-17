package app.izhang.topratedmovies;

/**
 * Created by ivanzhang on 9/8/17.
 *
 * - HomeActivity shows the queries data of movie posters and allows users to navigate to them
 * - Also provides the ability for the user to toggle between the sorting of top rated or most popular
 */

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import android.content.Loader;
import android.app.LoaderManager;

import java.util.ArrayList;
import java.util.List;

import app.izhang.topratedmovies.data.Movie;
import app.izhang.topratedmovies.utilities.NetworkUtils;
import app.izhang.topratedmovies.utilities.MovieLoader;


public class HomeActivity extends AppCompatActivity implements MovieViewAdapter.MovieViewAdapterOnClickHander, LoaderManager.LoaderCallbacks<List<Movie>>{

    private RecyclerView mMoviesRV;
    private List<Movie> movieList;
    private MovieViewAdapter mMovieAdapter;
    private MovieLoader mMovieLoader;

    private final static int LOADER_ID = 1001;

    private String TAG = "HomeActivity.java";
    private String MENU_KEY = "menu_loader_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TAG = getClass().toString();

        showUI();
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    /**
     * initializes the UI views
     *
     */
    private void showUI(){
        mMoviesRV = (RecyclerView) findViewById(R.id.rv_movies);
        mMovieAdapter = new MovieViewAdapter(this);

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this, layoutCountBasedOnOrientation());

        mMoviesRV.setLayoutManager(gridLayoutManager);
        mMoviesRV.setAdapter(mMovieAdapter);
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
    public void onItemListClick(String item) {
        Toast.makeText(this, "Item Clicked: " + item, Toast.LENGTH_SHORT).show();
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
                bundle.putInt(MENU_KEY, NetworkUtils.MOST_POPULAR);
                getLoaderManager().restartLoader(LOADER_ID, bundle, this);
                break;
            case R.id.action_top_rated:
                bundle.putInt(MENU_KEY, NetworkUtils.TOP_RATED);
                getLoaderManager().restartLoader(LOADER_ID, bundle, this);
                break;
        }

        return true;
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.v(TAG, "onCreateLoader()");
        int passedSort = 0;
        if(args != null) passedSort = args.getInt(MENU_KEY);
        mMovieLoader = new MovieLoader(getApplicationContext(), passedSort);
        return mMovieLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.v(TAG, "onLoadFinished()");
        movieList = data;
        mMovieAdapter.setData((ArrayList<Movie>) movieList);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.v(TAG, "onLoaderReset()");
        showUI();
    }

}
