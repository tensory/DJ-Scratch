package net.tensory.djscratch.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.tensory.djscratch.R;
import net.tensory.djscratch.listeners.EndlessScrollListener;
import net.tensory.djscratch.listeners.OnListItemScrollListener;
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
public class TimelineFragment extends Fragment implements OnScrollEventCallback {
    private ActionBarActivity actionBarActivity;
    private TweetsAdapter tweetsAdapter;
    private SoundController soundController;
    private boolean isPlaying;
    private boolean isMuted;
    private RequestLoader requestLoader;
    private int startingPage = 0;

    private abstract class RequestLoader implements Consumer<TweetsDataSource>, EndlessScrollListener.EndlessScrollLoader {
        private boolean isLoading;

        @Override
        public void load(int page) {
            isLoading = true;
            onLoad(page);
        }

        protected abstract void onLoad(int page);

        @Override
        public void setLoading(boolean loading) {
            this.isLoading = loading;
        }

        @Override
        public boolean isLoading() {
            return isLoading;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        this.requestLoader = new RequestLoader() {
            @Override
            protected void onLoad(int page) {
                new TimelineRequest(getActivity()).get(page, this);
            }

            @Override
            public void onSuccess(TweetsDataSource result) {
                tweetsAdapter.setData(result);
                this.setLoading(false);
            }

            @Override
            public void onFailure(int statusCode) {
                this.setLoading(false);
                String message;
                switch (statusCode) {
                    case 429:
                        message = "Somebody called the cops!\nRate limit exceeded.";
                        break;
                    default:
                        message = "Could not retrieve tweets";
                        break;
                }
                Toast.makeText(getActivity(), message, (statusCode == 429) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_timeline, menu);
        menu.findItem(R.id.volume_on).setVisible(!isMuted);
        menu.findItem(R.id.volume_off).setVisible(isMuted);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.volume_on:
                isPlaying = false;
                if (soundController != null) {
                    soundController.stop();
                }
                isMuted = true;
                actionBarActivity.supportInvalidateOptionsMenu();
                return true;
            case R.id.volume_off:
                isMuted = false;
                actionBarActivity.supportInvalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


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
        rvTweetsList.addOnScrollListener(new OnListItemScrollListener(new EndlessScrollListener(requestLoader, 0)));

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
        requestLoader.load(startingPage);
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
        if (!isPlaying && !isMuted) {
            soundController.start();
            isPlaying = true;
        }
    }
}
