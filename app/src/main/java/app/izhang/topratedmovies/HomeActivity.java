package app.izhang.topratedmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity {

    RecyclerView mMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
    }
}
