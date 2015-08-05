package net.tensory.djscratch;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import net.tensory.djscratch.fragments.LoginFragment;
import net.tensory.djscratch.fragments.TimelineFragment;
import net.tensory.djscratch.sound.SoundController;

import java.io.IOException;

/**
 * Activity displaying the user's Twitter timeline.
 * Reacts to scrolling events by playing a sound.
 */

public class MainActivity extends FragmentActivity implements LoginFragment.OnLoginResponseListener, SoundController {

    // On successful Twitter login response
    @Override
    public void onSuccess() {
        showTimelineFragment();
    }

    // On Twitter login failure
    @Override
    public void onFailure(Exception e) {
        Toast.makeText(this, getString(R.string.txt_login_error), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialView();

        initializeSuperpowered();
    }

    private void initializeSuperpowered() {

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

        // https://www.freesound.org/people/xtrgamr/sounds/257777/
        AssetFileDescriptor fd0 = getResources().openRawResourceFd(R.raw.vinyl_scratch);
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

    // Superpowered initialization
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
        ft.replace(R.id.fragment_container, new TimelineFragment());
        ft.commit();
    }

    // SoundController.start
    @Override
    public void start() {
        onPlayPause(true);
    }

    // SoundController.stop
    @Override
    public void stop() {
        onPlayPause(false);
    }
}
