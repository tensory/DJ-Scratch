package net.tensory.djscratch.rest;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * OAuth-based Twitter client with limited URL access.
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "4qHMF0QAvNflm8LYXuySFfxzv";
    public static final String REST_CONSUMER_SECRET = "eP4FGE5AIKfT7CDmqZc8CC3kSNkr2hxrSJPaesB3X6CrpFwkM6";
    public static final String REST_CALLBACK_URL = "x-oauthflow-twitter://djscratch";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL,
                REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        client.get(apiUrl, params, handler);
    }
}
