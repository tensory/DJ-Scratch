package net.tensory.djscratch.views;

/**
 * Created by ari on 8/2/15.
 */
public interface ScrollDetectingView {
    public void onScrollStart();
    public void onScrollEnd();
    public void onFlingStart();
    public void onFlingEnd();
}
