package net.tensory.djscratch.listeners;

/**
 * Implement this interface to define methods for scrolling behavior
 * with OnScrollGestureListener.
 */
public interface OnScrollEventCallback {
    public void onStop();

    public void onScrollUp();

    public void onScrollDown();

    public void onFlingUp();

    public void onFlingDown();
}
