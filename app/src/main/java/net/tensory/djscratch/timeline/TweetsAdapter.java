package net.tensory.djscratch.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * View holder for an individual tweet.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {
    private TweetsDataSource tweetDataSource;

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder tweetViewHolder, int i) {

    }

    public void setData(TweetsDataSource source) {
        this.tweetDataSource = source;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tweetDataSource.getCount();
    }
}
