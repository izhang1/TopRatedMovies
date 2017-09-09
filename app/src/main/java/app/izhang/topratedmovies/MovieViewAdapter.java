package app.izhang.topratedmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.izhang.topratedmovies.data.Movie;
import app.izhang.topratedmovies.utilities.PosterPathUtils;

/**
 * Created by ivanzhang on 9/8/17.
 */

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.MovieViewHolder> {

    private ArrayList<Movie> movieData;
    private Context context;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movieData.get(position);
        String posterPath = PosterPathUtils.buildPosterURL(movie.getPoster_path(), "size");
        Picasso.with(context).load(posterPath).into(holder.mPosterImageView);
    }


    @Override
    public int getItemCount() {
        if(movieData != null) return movieData.size();
        else return 0;
    }

    public void setData(ArrayList<Movie> data){
        this.movieData = data;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        private ImageView mPosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.poster_imageview);
            // Onclick mPosterImageView.setOnClickListener(this);

        }
    }
}
