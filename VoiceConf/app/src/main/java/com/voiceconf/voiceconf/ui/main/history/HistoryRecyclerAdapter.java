package com.voiceconf.voiceconf.ui.main.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.Conference;
import com.voiceconf.voiceconf.ui.view.PlaceholderRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attila Blenesi on 02 Jan 2016
 */
public class HistoryRecyclerAdapter extends PlaceholderRecyclerView.Adapter {

    //region VARIABLES
    private View.OnClickListener mOnClickListener;
    private List<Conference> mConferences;
    //endregion

    public HistoryRecyclerAdapter(View.OnClickListener onIOnClickListener) {
        mConferences = new ArrayList<>();
        mOnClickListener = onIOnClickListener;
    }

    public void update(@NonNull List<Conference> conferences) {
        mConferences.clear();
        mConferences.addAll(conferences);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conference, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new ConferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ConferenceViewHolder) holder).setup(mConferences.get(position));
    }

    @Override
    public int getItemCount() {
        return mConferences.size();
    }
}
