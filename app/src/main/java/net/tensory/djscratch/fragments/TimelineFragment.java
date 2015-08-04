package net.tensory.djscratch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.tensory.djscratch.R;
import net.tensory.djscratch.rest.Consumer;
import net.tensory.djscratch.rest.TimelineRequest;
import net.tensory.djscratch.timeline.TweetsAdapter;
import net.tensory.djscratch.timeline.TweetsDataSource;

/**
 * View for the Twitter timeline.
 */
public class TimelineFragment extends Fragment implements Consumer<TweetsDataSource> {
    TweetsAdapter tweetsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_timeline, container, false);

        RecyclerView rvTweetsList = (RecyclerView) view.findViewById(R.id.rv_tweets_list);
        rvTweetsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        
        tweetsAdapter = new TweetsAdapter(this.getActivity());
        rvTweetsList.setAdapter(tweetsAdapter);
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

    }
}
