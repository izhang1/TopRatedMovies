package app.izhang.topratedmovies;

import android.content.Context;
import android.content.Intent;
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
 * - MovieViewAdapter class
 * - Setup the RecyclerView with the passed in data and lays them out according to our list item
 */

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.MovieViewHolder>{

    private ArrayList<Movie> movieData;
    private Context context;

    final private MovieViewAdapterOnClickHander mClickHandler;

    public MovieViewAdapter(MovieViewAdapterOnClickHander movieViewAdapterOnClickHander){
        this.mClickHandler = movieViewAdapterOnClickHander;
    }

    public interface MovieViewAdapterOnClickHander{
        void onItemListClick(String item);
    }
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


    /**
     * MovieViewHolder class - inner
     * - Initalizes the view and sets the value as well as implements the onclick functions
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mPosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.poster_imageview);
            mPosterImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Movie clickedMovie = movieData.get(getAdapterPosition());
            Intent detailView = new Intent(context, DetailActivity.class);
            detailView.putExtra(context.getString(R.string.key_movie_object), clickedMovie);
            context.startActivity(detailView);
        }
    }
}
