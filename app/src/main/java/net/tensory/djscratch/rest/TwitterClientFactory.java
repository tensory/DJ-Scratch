package net.tensory.djscratch.rest;

import android.content.Context;

/**
 * Retrieve a TwitterClient instance.
 */
class TwitterClientFactory {
    public static TwitterClient getTwitterClient(Context context) {
        return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, context);
    }
}
