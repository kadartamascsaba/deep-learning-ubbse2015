package com.voiceconf.voiceconf.ui.conference.setup;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;
import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.ui.view.BaseFriendViewHolder;
import com.voiceconf.voiceconf.ui.view.PlaceholderRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attila Blenesi on 01 Jan 2016
 */
public class InviteesAdapter extends PlaceholderRecyclerView.Adapter {

    private View.OnClickListener mOnClickListener;

    private List<ParseUser> mUsers;

    public InviteesAdapter(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        mUsers = new ArrayList<>();
    }

    //region HELPER
    public void update(@NonNull List<ParseUser> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    public List<String> getItemIds(){
        List<String> ids = new ArrayList<>();
        for (ParseUser user : mUsers){
            ids.add(user.getObjectId());
        }
        return ids;
    }
    //endregion


    //region ADApTER METHODS
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new BaseFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseFriendViewHolder) holder).setup(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    //endregion

}
