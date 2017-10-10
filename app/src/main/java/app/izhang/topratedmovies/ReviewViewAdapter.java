package app.izhang.topratedmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ivanzhang on 9/25/17.
 * - ReviewViewAdapter class
 * - Setup the RecyclerView with the passed in data and lays them out according to our list item
 * - This view is shown on the DetailActivity.class
 */

public class ReviewViewAdapter extends RecyclerView.Adapter<ReviewViewAdapter.ReviewViewHolder>{

    private ArrayList<String> mReviewData;
    private Context mContext;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.mReviewText.setText(mReviewData.get(position));
    }

    @Override
    public int getItemCount() {
        if(mReviewData != null) return mReviewData.size();
        else return 0;
    }

    public void setData(ArrayList<String> data){
        this.mReviewData = data;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        private TextView mReviewText;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mReviewText = (TextView) itemView.findViewById(R.id.tv_review_text);
        }

    }
}
