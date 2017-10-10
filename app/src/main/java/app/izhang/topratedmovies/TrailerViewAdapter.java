package app.izhang.topratedmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import app.izhang.topratedmovies.data.Trailer;

/**
 * Created by ivanzhang on 9/24/17.
 * - TrailerViewAdapter class
 * - Setup the RecyclerView with trailer data showing the name
 * - onClick, creates and starts a YouTube intent to show the trailer
 *
 */

public class TrailerViewAdapter extends RecyclerView.Adapter<TrailerViewAdapter.TrailerViewHolder>{

    private ArrayList<Trailer> mTrailerData;
    private Context mContext;

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflator = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflator.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailerData.get(position);
        holder.mTrailerName.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if(mTrailerData != null) return mTrailerData.size();
        return 0;
    }

    public void setData(ArrayList<Trailer> trailerData){
        this.mTrailerData = trailerData;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTrailerName;

        public TrailerViewHolder(View itemView){
            super(itemView);
            mTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Trailer trailer = mTrailerData.get(getAdapterPosition());
            Intent trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.yt_base_url) + trailer.getSource()));
            mContext.startActivity(trailerIntent);
        }
    }
}
