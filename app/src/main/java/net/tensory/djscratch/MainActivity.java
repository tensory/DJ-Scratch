package net.tensory.djscratch;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.tensory.djscratch.fragments.HomeTimelineFragment;
import net.tensory.djscratch.fragments.LoginFragment;
import net.tensory.djscratch.rest.TwitterClientFactory;
import net.tensory.djscratch.views.ScrollDetectingView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Activity displaying the user's Twitter timeline.
 * Reacts to scrolling events by playing a sound.
 */

public class MainActivity extends FragmentActivity implements ScrollDetectingView, LoginFragment.OnLoginResponseListener {
//    private GestureDetectorCompat mDetector;

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

    public void onLoginSuccess() {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container,
//                        new HomeTimelineFragment(),
//                        HomeTimelineFragment.TAG)
//                .commit();
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setEnabled(false);

        TwitterClientFactory.getTwitterClient(this).getHomeTimeline(0, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }
        });
    }

    // On successful Twitter login response
    @Override
    public void onSuccess() {
        showTimelineFragment();
    }

    // On Twitter login failure
    @Override
    public void onFailure(Exception e) {
        Toast.makeText(this, "Shanope", Toast.LENGTH_SHORT).show();
        e.printStackTrace();

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
/*
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialView();

        // Set up gesture detection.
        // mDetector = new GestureDetectorCompat(this, new MyGestureListener(this));

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

    private native void SuperpoweredPlayer(String apkPath, long[] offsetAndLength);

    private native void onPlayPause(boolean play);

    static {
        System.loadLibrary("SuperpoweredPlayer");
    }

    // View setup
    private void setInitialView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new LoginFragment());
        ft.commit();
    }

    private void showTimelineFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new HomeTimelineFragment());
        ft.commit();
    }
}
