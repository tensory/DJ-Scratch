package net.tensory.djscratch.timeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.tensory.djscratch.R;

/**
 * View holder for an individual tweet.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {
    private TweetsDataSource tweetDataSource;
    private Context context;

    public TweetsAdapter(Context context) {
        this.context = context;
        this.tweetDataSource = new TweetsDataSource();
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_tweet, viewGroup, false);
        return new TweetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder tweetViewHolder, int i) {
        Tweet tweet = tweetDataSource.getTweets().get(i);
        tweetViewHolder.setAuthorHandle(tweet.getScreenName());
        tweetViewHolder.setTweetText(tweet.getText());
        tweetViewHolder.setProfileImage(context, tweet.getProfileImageUrl());
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
