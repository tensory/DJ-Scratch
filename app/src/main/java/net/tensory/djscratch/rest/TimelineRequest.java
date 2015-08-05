package net.tensory.djscratch.rest;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.tensory.djscratch.timeline.TweetsDataSource;

import org.apache.http.Header;
import org.json.JSONArray;
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
                TweetsDataSource result = TimelineParser.parse(response);
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
