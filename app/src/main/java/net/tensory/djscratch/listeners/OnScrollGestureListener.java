package net.tensory.djscratch.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Touch listener for taps on RecyclerViews
 * which is capable of tracking gestures and dispatching events.
 */
public class OnScrollGestureListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;

    private static final String DEBUG_TAG = "RecyclerItemOnTouchListener";

    public OnScrollGestureListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent event1, MotionEvent event2,
                                   float velocityX, float velocityY) {

                Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
//                togglePlayPause(event1, event2);
                return false; // do not disrupt event propagation
            }

            @Override
            public boolean onScroll(MotionEvent event1, MotionEvent event2,
                                    float distanceX, float distanceY) {
                Log.d(DEBUG_TAG, "onScroll: " + event1.toString()+event2.toString());
                Log.d(DEBUG_TAG, "onScroll: " + "distanceY " + distanceY);
//                togglePlayPause(event1, event2);
                return false; // do not disrupt event propagation
            }
//
//            private void togglePlayPause(MotionEvent event1, MotionEvent event2) {
//                if (event1.getAction() == MotionEvent.ACTION_DOWN && event2.getAction() == MotionEvent.ACTION_MOVE) {
//                    if (!isGestureActive) {
//                        isGestureActive = true;
//                        onPlayPause(isGestureActive);
//                    }
//                } else if (event1.getAction() == MotionEvent.ACTION_DOWN && event2.getAction() == MotionEvent.ACTION_UP) {
//                    if (isGestureActive) {
//                        isGestureActive = false;
//                        onPlayPause(isGestureActive);
//                    }
//                }
//            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        this.gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        int i = 1;
    }
}
