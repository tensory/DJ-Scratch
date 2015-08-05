package net.tensory.djscratch.listeners;

import android.widget.AbsListView;

/**
 * Use this object with a ScrollListener to help compare
 * a previous onScroll event state with the new one.
 */
class ScrollStateManager {
    private int currentScrollState;

    public ScrollStateManager(int scrollState) {
        currentScrollState = scrollState;
    }

    public void setNewScrollState(int newScrollState) {
        currentScrollState = newScrollState;
    }

    public int getScrollState() {
        return currentScrollState;
    }

    /**
     * Get the platform definition for an IDLE state.
     *
     * @return int SCROLL_STATE_IDLE
     */
    public static final int getIdle() {
        return AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    }

    /**
     * Get the platform definition for a SCROLL state.
     *
     * @return int SCROLL_STATE_TOUCH_SCROLL;
     */
    public static final int getScroll() {
        return AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
    }

    /**
     * Get the platform definition for a FLING state.
     *
     * @return int SCROLL_STATE_FLING;
     */
    public static final int getFling() {
        return AbsListView.OnScrollListener.SCROLL_STATE_FLING;
    }
}
