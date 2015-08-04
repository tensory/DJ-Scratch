package net.tensory.djscratch.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.tensory.djscratch.R;

/**
 * View for the Twitter timeline.
 */
public class HomeTimelineFragment extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_timeline, container, false);

        RecyclerView rvTweetsList = (RecyclerView) view.findViewById(R.id.rv_tweets_list);
        rvTweetsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        return view;
    }
}
