package app.izhang.topratedmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.izhang.topratedmovies.data.Movie;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle receivedData = getIntent().getExtras();
        Movie currentMovie = (Movie) receivedData.get(getString(R.string.key_movie_object));

        setTitle(currentMovie.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
