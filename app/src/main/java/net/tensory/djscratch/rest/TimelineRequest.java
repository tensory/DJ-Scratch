package net.tensory.djscratch.rest;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.tensory.djscratch.timeline.Tweet;
import net.tensory.djscratch.timeline.TweetsDataSource;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent an API request to a particular endpoint.
 */
public class TimelineRequest {
    private Context context;

    public TimelineRequest(Context context) {
        this.context = context;
    }

    public void get(final Consumer<TweetsDataSource> consumer) {
        TwitterClientFactory.getTwitterClient(this.context).getHomeTimeline(0, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                TweetsDataSource result = new TweetsDataSource();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject o = response.getJSONObject(i);
                        int id = o.getInt("id");
                        String text = o.getString("text");

                        JSONObject user = o.getJSONObject("user");
                        String screenName = user.getString("screen_name");
                        String userIconUrl = user.getString("profile_image_url");

                        Tweet tweet = new Tweet();
                        tweet.setId(id);
                        tweet.setScreenName(screenName);
                        tweet.setProfileImageUrl(userIconUrl);
                        tweet.setText(text);

                        result.add(tweet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                consumer.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
                consumer.onFailure(statusCode);
            }
        });
    }
}
