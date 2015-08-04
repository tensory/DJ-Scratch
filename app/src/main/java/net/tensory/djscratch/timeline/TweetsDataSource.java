package net.tensory.djscratch.timeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for tweets data.
 */
public class TweetsDataSource {
    private List<Tweet> tweetsList;

    public TweetsDataSource() {
        this.tweetsList = new ArrayList<>();
    }

    public List<Tweet> getTweets() {
        return this.tweetsList;
    }

    public int getCount() {
        return this.tweetsList.size();
    }

    public void add(Tweet tweet) {
        this.tweetsList.add(tweet);
    }
}
