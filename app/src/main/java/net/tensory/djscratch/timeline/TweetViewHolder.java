package net.tensory.djscratch.timeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.tensory.djscratch.R;

/**
 * View holder representing a Tweet.
 */
public class TweetViewHolder extends RecyclerView.ViewHolder {
    private TextView tvAuthorScreenName;
    private TextView tvText;
    private ImageView ivProfileImage;

    public TweetViewHolder(View itemView) {
        super(itemView);

        tvAuthorScreenName = (TextView) itemView.findViewById(R.id.tv_twitter_handle);
        tvText = (TextView) itemView.findViewById(R.id.tv_tweet_text);
        ivProfileImage = (ImageView) itemView.findViewById(R.id.iv_profile_image);
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

    public void setProfileImage(Context context, String url) {
        int dimen = ivProfileImage.getLayoutParams().width;

        Picasso.with(context)
                .load(url)
                .resize(dimen, dimen)
                .into(ivProfileImage);
    }
}
