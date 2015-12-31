package com.voiceconf.voiceconf.ui.main.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.networking.services.FriendService;
import com.voiceconf.voiceconf.storage.models.Friend;
import com.voiceconf.voiceconf.storage.nonpersistent.DataManager;
import com.voiceconf.voiceconf.storage.nonpersistent.VoiceConfApplication;
import com.voiceconf.voiceconf.ui.view.RecyclerViewWithPlaceholder;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Attila Blenesi on 25 Dec 2015
 */
public class FriendsFragment extends Fragment implements Observer {

    private RecyclerViewWithPlaceholder mRecyclerView;
    private FriendRecyclerAdapter mRecyclerAdapter;
    private SwipeRefreshLayout mSwipeContainer;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerAdapter = new FriendRecyclerAdapter(null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accept
                mSwipeContainer.setRefreshing(true);
                FriendService.acceptFriend((Friend) v.getTag());
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decline
                mSwipeContainer.setRefreshing(true);
                FriendService.archiveFriend((Friend) v.getTag(), true);
            }
        });
        mRecyclerView = (RecyclerViewWithPlaceholder) view.findViewById(R.id.friend_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setEmptyView(view.findViewById(R.id.friend_placeholder));

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.friend_swipe_refresh);
        mSwipeContainer.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FriendService.getFriends(null);
                mSwipeContainer.setRefreshing(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FriendService.getFriends(null);
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
        if ((int) data == DataManager.FRIENDS_UPDATED) {
            mRecyclerAdapter.update(VoiceConfApplication.sDataManager.getFriends());
            mSwipeContainer.setRefreshing(false);
            mRecyclerView.updateEmptyView(true);
        }
    }
}
