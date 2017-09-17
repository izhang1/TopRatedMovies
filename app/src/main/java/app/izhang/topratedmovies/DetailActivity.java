package app.izhang.topratedmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.izhang.topratedmovies.data.Movie;
import app.izhang.topratedmovies.utilities.PosterPathUtils;

public class DetailActivity extends AppCompatActivity {

    public TextView mVoteAverageTV;
    public TextView mOverviewTV;
    public TextView mReleaseDateTV;

    public ImageView mPosterView;

    public Movie mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle receivedData = getIntent().getExtras();
        if(receivedData != null) {
            mMovieData = (Movie) receivedData.get(getString(R.string.key_movie_object));
        }

        setTitle(mMovieData.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showUI();
    }

    public void showUI(){

        mPosterView = (ImageView) findViewById(R.id.poster_imageview);
        String posterPath = PosterPathUtils.buildPosterURL(mMovieData.getPoster_path(), "size");
        Picasso.with(this).load(posterPath).into(mPosterView);

        mVoteAverageTV = (TextView) findViewById(R.id.tv_vote_average);
        mOverviewTV = (TextView) findViewById(R.id.tv_overview);
        mReleaseDateTV = (TextView) findViewById(R.id.tv_release_date);

        mVoteAverageTV.setText("Vote Average: " + mMovieData.getVote_average());
        mOverviewTV.setText("Overview: " + mMovieData.getOverview());
        mReleaseDateTV.setText("Release Date: " + mMovieData.getRelease_date());
    }
}
