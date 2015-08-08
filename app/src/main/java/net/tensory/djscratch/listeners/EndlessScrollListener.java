package net.tensory.djscratch.listeners;

import android.widget.AbsListView;

/**
 * Via https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;

    // Communicate with the Loader that loads more data.
    private EndlessScrollLoader mLoader;

    public interface EndlessScrollLoader {
        public boolean isLoading();
        public void setLoading(boolean loading);
        public void load(int page);
    }

    public EndlessScrollListener(EndlessScrollLoader loader, int startPage) {
        currentPage = startPage;
        mLoader = loader;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!mLoader.isLoading() && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            currentPage++;
            mLoader.load(currentPage);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}