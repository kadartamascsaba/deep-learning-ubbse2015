package com.voiceconf.voiceconf.ui.main.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.networking.services.ConferenceService;
import com.voiceconf.voiceconf.networking.services.FriendService;
import com.voiceconf.voiceconf.storage.models.Conference;
import com.voiceconf.voiceconf.storage.models.Friend;
import com.voiceconf.voiceconf.storage.nonpersistent.DataManager;
import com.voiceconf.voiceconf.storage.nonpersistent.VoiceConfApplication;
import com.voiceconf.voiceconf.ui.conference.ConferenceActivity;
import com.voiceconf.voiceconf.ui.main.friends.FriendRecyclerAdapter;
import com.voiceconf.voiceconf.ui.view.PlaceholderRecyclerView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Attila Blenesi on 25 Dec 2015
 */
public class HistoryFragment extends Fragment implements Observer{

    private PlaceholderRecyclerView mRecyclerView;
    private HistoryRecyclerAdapter mRecyclerAdapter;
    private SwipeRefreshLayout mSwipeContainer;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerAdapter = new HistoryRecyclerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ConferenceActivity.getStartIntent(getContext(),(Conference) v.getTag()));
            }
        });
        mRecyclerView = (PlaceholderRecyclerView) view.findViewById(R.id.history_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setEmptyView(view.findViewById(R.id.history_placeholder));

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.history_swipe_refresh);
        mSwipeContainer.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConferenceService.getConferences(null);
                mSwipeContainer.setRefreshing(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ConferenceService.getConferences(null);
        mSwipeContainer.post(new Runnable() {
            @Override
            public void run() {
                mSwipeContainer.setRefreshing(true);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VoiceConfApplication.sDataManager.addObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VoiceConfApplication.sDataManager.deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if ((int) data == DataManager.CONFERENCE_UPDATED) {
            mRecyclerAdapter.update(VoiceConfApplication.sDataManager.getConferences());
            mSwipeContainer.setRefreshing(false);
            mRecyclerView.updateEmptyView(true);
        }
    }
}
