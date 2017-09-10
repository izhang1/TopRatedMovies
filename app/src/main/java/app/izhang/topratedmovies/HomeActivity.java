package app.izhang.topratedmovies;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.izhang.topratedmovies.data.Movie;
import app.izhang.topratedmovies.utilities.MovieJsonUtils;
import app.izhang.topratedmovies.utilities.NetworkUtils;

public class HomeActivity extends AppCompatActivity implements MovieViewAdapter.MovieViewAdapterOnClickHander{

    public RecyclerView mMoviesRV;
    public List<Movie> movieList;
    public MovieViewAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMoviesRV = (RecyclerView) findViewById(R.id.rv_movies);
        mMovieAdapter = new MovieViewAdapter();

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this, 2);

        mMoviesRV.setLayoutManager(gridLayoutManager);
        mMoviesRV.setAdapter(mMovieAdapter);

        new queryMovieDb().execute("");
    }

    public class queryMovieDb extends AsyncTask<String, Integer, List<Movie>>{
        private String TAG = getClass().toString();

        @Override
        protected List<Movie> doInBackground(String... params) {
            URL queryUrl = NetworkUtils.buildUrl("");
            Log.v(TAG, "Query URL: " + queryUrl.toString());
            try{
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);
                Log.v(TAG, jsonResponse);

                List<Movie> parsedMovieList = MovieJsonUtils.getMovieListFromJson(getApplicationContext(), jsonResponse);
                return parsedMovieList;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            movieList = movies;
            mMovieAdapter.setData((ArrayList<Movie>) movieList);
            super.onPostExecute(movies);
        }
    }
}
