package net.tensory.djscratch.listeners;

import android.widget.AbsListView;

/**
 * Use this object with a ScrollListener to help compare
 * a previous onScroll event state with the new one.
 */
class ScrollStateManager {
    // 0 = penultimate scroll state
    // 1 = last scroll state
    private int[] currentScrollState;

    public ScrollStateManager(int scrollState) {
        currentScrollState = new int[2];
        currentScrollState[0] = 0;
        currentScrollState[1] = scrollState;
    }

    public void setNewScrollState(int newScrollState) {
        currentScrollState[0] = currentScrollState[1];
        currentScrollState[1] = newScrollState;
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

    public boolean isIdle() {
        return currentScrollState[1] == getIdle();
    }

    public boolean isScrolling() {
        return currentScrollState[0] != getScroll() && currentScrollState[1] == getScroll();
    }

    public boolean wasFlung() {
        return currentScrollState[0] != getFling() && currentScrollState[1] == getFling();
    }
}
