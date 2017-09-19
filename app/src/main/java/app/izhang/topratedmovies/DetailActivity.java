package app.izhang.topratedmovies;

/*
  Created by ivanzhang on 9/8/17.

  - DetailActivity showing additional data from the movie attributes
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.izhang.topratedmovies.data.Movie;
import app.izhang.topratedmovies.utilities.PosterPathUtils;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle receivedData = getIntent().getExtras();
        if(receivedData != null) {
            mMovieData = (Movie) receivedData.get(getString(R.string.key_movie_object));
        }

        setTitle(mMovieData != null ? mMovieData.getTitle() : null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showUI();
    }


    /**
     * initializes the UI views and populates them with the passed data from mMovieData
     *
     */
    private void showUI(){
        ImageView mPosterView = (ImageView) findViewById(R.id.poster_imageview);
        String posterPath = PosterPathUtils.buildPosterURL(mMovieData.getPoster_path(), "size");
        Picasso.with(this).load(posterPath).into(mPosterView);

        TextView mVoteAverageTV = (TextView) findViewById(R.id.tv_vote_average);
        TextView mOverviewTV = (TextView) findViewById(R.id.tv_overview);
        TextView mReleaseDateTV = (TextView) findViewById(R.id.tv_release_date);

        mVoteAverageTV.setText(getString(R.string.vote_average_label) + mMovieData.getVote_average());
        mOverviewTV.setText(getString(R.string.overview_label) + mMovieData.getOverview());
        mReleaseDateTV.setText(getString(R.string.release_date_label) + mMovieData.getRelease_date());
    }
}
