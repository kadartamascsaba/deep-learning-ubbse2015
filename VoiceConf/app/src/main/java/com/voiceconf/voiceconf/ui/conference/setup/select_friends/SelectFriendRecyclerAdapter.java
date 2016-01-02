package com.voiceconf.voiceconf.ui.conference.setup.select_friends;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;
import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.ui.view.PlaceholderRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Attila Blenesi on 31 Dec 2015
 */
public class SelectFriendRecyclerAdapter extends PlaceholderRecyclerView.Adapter {

    //region VARIABLES
    private View.OnClickListener mOnClickListener;
    private List<ParseUser> mUsers;
    private Map<Integer, Boolean> mSelected;
    //endregion

    //region CONSTRUCTOR
    public SelectFriendRecyclerAdapter(View.OnClickListener onClickListener){
        mOnClickListener = onClickListener;
        mUsers = new ArrayList<>();
        mSelected = new HashMap<>();
    }
    //endregion

    //region HELPER
    public void update(@NonNull List<ParseUser> users, @NonNull Map<Integer, Boolean> selected){
        mUsers.clear();
        mUsers.addAll(users);

        mSelected.clear();
        mSelected.putAll(selected);
        notifyDataSetChanged();
    }

    public void updateSelections(@NonNull Map<Integer, Boolean> selected) {
        mSelected.clear();
        mSelected.putAll(selected);
        notifyDataSetChanged();
    }
    //endregion

    //region ADAPTER METHODS
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent, false);
        view.setOnClickListener(mOnClickListener);
        return  new SelectFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SelectFriendViewHolder) holder).setup(mUsers.get(position), mSelected.containsKey(position) ? mSelected.get(position): false);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    //endregion
}
