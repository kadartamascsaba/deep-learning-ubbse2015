package com.voiceconf.voiceconf.ui.conference;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.Invite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attila Blenesi on 02 Jan 2016
 */
public class InviteeAdapter extends RecyclerView.Adapter {

    private List<Invite> mInvitees;
    private boolean mSmall;

    public InviteeAdapter(boolean small){
        mInvitees = new ArrayList<>();
        mSmall = small;
    }
    public void update(@NonNull List<Invite> invitees) {
        mInvitees.clear();
        mInvitees.addAll(invitees);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mSmall ? R.layout.item_friend_horizontal_small : R.layout.item_friend_horizontal, parent, false);
        return new FriendHorizontalViewHolder(view);    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FriendHorizontalViewHolder) holder).setup(mInvitees.get(position));
    }

    @Override
    public int getItemCount() {
        return mInvitees.size();
    }
}
