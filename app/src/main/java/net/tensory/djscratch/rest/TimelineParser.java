package net.tensory.djscratch.rest;

import net.tensory.djscratch.timeline.Tweet;
import net.tensory.djscratch.timeline.TweetsDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Transform JSON results.
 */
public class TimelineParser {

    public static TweetsDataSource parse(JSONArray response) {
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
        return result;
    }
}
