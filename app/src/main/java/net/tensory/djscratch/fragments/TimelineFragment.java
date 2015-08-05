package net.tensory.djscratch.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.tensory.djscratch.R;
import net.tensory.djscratch.listeners.OnScrollEventCallback;
import net.tensory.djscratch.listeners.OnScrollGestureListener;
import net.tensory.djscratch.rest.Consumer;
import net.tensory.djscratch.rest.TimelineRequest;
import net.tensory.djscratch.sound.SoundController;
import net.tensory.djscratch.timeline.TweetsAdapter;
import net.tensory.djscratch.timeline.TweetsDataSource;

/**
 * View for the Twitter timeline.
 */
public class TimelineFragment extends Fragment implements Consumer<TweetsDataSource>, OnScrollEventCallback {
    private ActionBarActivity actionBarActivity;
    private TweetsAdapter tweetsAdapter;
    private SoundController soundController;
    private boolean isPlaying;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            soundController = (SoundController) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SoundController");
        }

        try {
            actionBarActivity = (ActionBarActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SoundController");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_timeline, container, false);

        RecyclerView rvTweetsList = (RecyclerView) view.findViewById(R.id.rv_tweets_list);
        rvTweetsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvTweetsList.addOnScrollListener(new OnScrollGestureListener(this));

        tweetsAdapter = new TweetsAdapter(this.getActivity());
        rvTweetsList.setAdapter(tweetsAdapter);

        // Configure toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            actionBarActivity.setSupportActionBar(toolbar);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new TimelineRequest(this.getActivity()).get(this);
    }

    @Override
    public void onSuccess(TweetsDataSource result) {
        tweetsAdapter.setData(result);
        tweetsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(int statusCode) {
        // no-op
    }

    // Methods for sound manipulation.
    @Override
    public void onScrollStop() {
        soundController.stop();
        isPlaying = false;
    }

    @Override
    public void onScrollUp() {
        startPlaying();
    }

    @Override
    public void onScrollDown() {
        startPlaying();
    }

    @Override
    public void onFlingUp() {
        startPlaying();
    }

    @Override
    public void onFlingDown() {
        startPlaying();
    }

    private void startPlaying() {
        if (!isPlaying) {
            soundController.start();
            isPlaying = true;
        }
    }
}
