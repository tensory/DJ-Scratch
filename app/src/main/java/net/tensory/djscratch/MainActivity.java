package net.tensory.djscratch;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import net.tensory.djscratch.views.ScrollDetectingView;

import java.io.IOException;

public class MainActivity extends Activity implements ScrollDetectingView {
    private GestureDetectorCompat mDetector;

    @Override
    public void onScrollStart() {
        onPlayPause(true);
    }

    @Override
    public void onScrollEnd() {
        onPlayPause(false);
    }

    @Override
    public void onFlingStart() {
        onPlayPause(true);
    }

    @Override
    public void onFlingEnd() {
        onPlayPause(false);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private ScrollDetectingView scrollDetector;
        private boolean isGestureActive;

        private static final String DEBUG_TAG = "Gestures";

        public MyGestureListener(ScrollDetectingView view) {
            this.scrollDetector = view;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            togglePlayPause(event1, event2);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2,
                               float distanceX, float distanceY) {
            Log.d(DEBUG_TAG, "onScroll: " + event1.toString()+event2.toString());
            Log.d(DEBUG_TAG, "onScroll: " + "distanceY " + distanceY);
            togglePlayPause(event1, event2);
            return true;
        }

        private void togglePlayPause(MotionEvent event1, MotionEvent event2) {
            if (event1.getAction() == MotionEvent.ACTION_DOWN && event2.getAction() == MotionEvent.ACTION_MOVE) {
                if (!isGestureActive) {
                    isGestureActive = true;
                    onPlayPause(isGestureActive);
                }
            } else if (event1.getAction() == MotionEvent.ACTION_DOWN && event2.getAction() == MotionEvent.ACTION_UP) {
                if (isGestureActive) {
                    isGestureActive = false;
                    onPlayPause(isGestureActive);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up gesture detection.
        mDetector = new GestureDetectorCompat(this, new MyGestureListener(this));

        // Set up sound playback.
        // Get the device's sample rate and buffer size to enable low-latency Android audio output, if available.
        String samplerateString = null, buffersizeString = null;
        if (Build.VERSION.SDK_INT >= 17) {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            samplerateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
            buffersizeString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
        }
        if (samplerateString == null) samplerateString = "44100";
        if (buffersizeString == null) buffersizeString = "512";

        AssetFileDescriptor fd0 = getResources().openRawResourceFd(R.raw.trumpet_sample);
        long[] params = {
                fd0.getStartOffset(),
                fd0.getLength(),
                Integer.parseInt(samplerateString),
                Integer.parseInt(buffersizeString)
        };

        try {
            fd0.getParcelFileDescriptor().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Arguments: path to the APK file, offset and length of the resource file, sample rate, audio buffer size.
        SuperpoweredPlayer(getPackageResourcePath(), params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private native void SuperpoweredPlayer(String apkPath, long[] offsetAndLength);

    private native void onPlayPause(boolean play);

    static {
        System.loadLibrary("SuperpoweredPlayer");
    }
}
