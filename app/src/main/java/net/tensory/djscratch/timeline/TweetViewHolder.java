package net.tensory.djscratch.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.tensory.djscratch.R;

/**
 * View holder representing a Tweet.
 */
public class TweetViewHolder extends RecyclerView.ViewHolder {
    private TextView tvAuthorScreenName;
    private TextView tvText;

    public TweetViewHolder(View itemView) {
        super(itemView);

        tvAuthorScreenName = (TextView) itemView.findViewById(R.id.tv_twitter_handle);
        tvText = (TextView) itemView.findViewById(R.id.tv_tweet_text);
    }

    public void setAuthorHandle(String screenName) {
        tvAuthorScreenName.setText(prepareScreenName(screenName));
    }

    public void setTweetText(String text) {
        tvText.setText(text);
    }

    private static String prepareScreenName(String input) {
        return String.format("@%s", input);
    }
}
