package com.voiceconf.voiceconf.ui.main.friends;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.Friend;
import com.voiceconf.voiceconf.ui.view.FriendViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attila Blenesi on 30 Dec 2015
 */
public class FriendRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //region VARIABLES
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mAcceptClickListener;
    private View.OnClickListener mDeclineClickListener;

    private List<Friend> mFriends;
    //endregion

    public FriendRecyclerAdapter(View.OnClickListener onItemClickListener, View.OnClickListener acceptClickListenter, View.OnClickListener declineClickListenter) {
        mFriends = new ArrayList<>();
        mOnClickListener = onItemClickListener;
        mAcceptClickListener = acceptClickListenter;
        mDeclineClickListener = declineClickListenter;
    }

    public void update(@NonNull List<Friend> friends) {
        mFriends.clear();
        mFriends.addAll(friends);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        view.setOnClickListener(mOnClickListener);
        view.findViewById(R.id.accept).setOnClickListener(mAcceptClickListener);
        view.findViewById(R.id.decline).setOnClickListener(mDeclineClickListener);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FriendViewHolder) holder).setup(mFriends.get(position));
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }
}
