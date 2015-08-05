package net.tensory.djscratch.listeners;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

public class OnScrollGestureListener extends RecyclerView.OnScrollListener {
    private ScrollStateManager scrollStateManager;
    private OnScrollEventCallback onScrollEventCallback;

    public OnScrollGestureListener(OnScrollEventCallback callback) {
        scrollStateManager = new ScrollStateManager(AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
        onScrollEventCallback = callback;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        scrollStateManager.setNewScrollState(newState);

        if (scrollStateManager.isIdle() && onScrollEventCallback != null) {
            onScrollEventCallback.onStop();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // Handle scroll event
        if (scrollStateManager.isScrolling()) {
            if (dy > 0) {
                onScrollEventCallback.onScrollUp();
            } else {
                onScrollEventCallback.onScrollDown();
            }
        } else if (scrollStateManager.wasFlung()) {
            if (dy > 0) {
                onScrollEventCallback.onFlingUp();
            } else {
                onScrollEventCallback.onFlingDown();
            }
        }

        super.onScrolled(recyclerView, dx, dy);
    }
}