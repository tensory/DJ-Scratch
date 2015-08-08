package net.tensory.djscratch.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AbsListView;

/**
 * Adapter scroll listener that extends the list item-based scroll concept
 * to a RecyclerView scroll listener.
 *
 * Inspired by https://gist.github.com/sjudd/0776594543c4b6c30d38
 */
public class OnListItemScrollListener extends RecyclerView.OnScrollListener {
    private final AbsListView.OnScrollListener scrollListener;
    private int previousFirstVisible = -1;
    private int previousVisibleCount = -1;
    private int previousItemCount = -1;

    public OnListItemScrollListener(AbsListView.OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        int firstVisible = layoutManager.findFirstVisibleItemPosition();
        int visibleCount = Math.abs(firstVisible - layoutManager.findLastVisibleItemPosition());
        int itemCount = recyclerView.getAdapter().getItemCount();

        if (firstVisible != previousFirstVisible || visibleCount != previousVisibleCount
                || itemCount != previousItemCount) {
            scrollListener.onScroll(null, firstVisible, visibleCount, itemCount);
            previousFirstVisible = firstVisible;
            previousVisibleCount = visibleCount;
            previousItemCount = itemCount;
        }
    }

}
