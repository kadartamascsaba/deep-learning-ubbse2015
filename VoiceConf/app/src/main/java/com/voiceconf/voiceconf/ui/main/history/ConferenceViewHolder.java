/**
 * Copyright 2016 Halcyon Mobile
 * http://www.halcyonmobile.com
 * All rights reserved.
 */
package com.voiceconf.voiceconf.ui.main.history;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.Conference;
import com.voiceconf.voiceconf.ui.conference.InviteeAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Attila Blenesi on 02 Jan 2016
 */
public class ConferenceViewHolder extends RecyclerView.ViewHolder {

    //region VARIABLES
    private CardView mCard;
    private TextView mTitle;
    private TextView mDate;
    private RecyclerView mFriedRecyclerView;
    //endregion

    //region CONSTRUCTOR
    public ConferenceViewHolder(View itemView) {
        super(itemView);
        mCard = (CardView) itemView.findViewById(R.id.conference_card);
        mTitle = (TextView) itemView.findViewById(R.id.conference_title);
        mDate = (TextView) itemView.findViewById(R.id.conference_date);
        mFriedRecyclerView = (RecyclerView) itemView.findViewById(R.id.conference_invitees);
    }
    //endregion

    //region HELPER
    public void setup(Conference conference) {
        itemView.setTag(conference);
        //Load conference data
        if (conference != null) {
            if (!TextUtils.isEmpty(conference.getTitle())) {
                mTitle.setText(conference.getTitle());
            }
            if (conference.getCreatedAt() != null){
                mDate.setText(new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault()).format(conference.getCreatedAt()));
            }
            if(!conference.isClosed()){
                mCard.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.colorAccent));
            }else{
                mCard.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.icons));
            }

            InviteeAdapter inviteeAdapter = new InviteeAdapter(true);
            mFriedRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            mFriedRecyclerView.setAdapter(inviteeAdapter);
            inviteeAdapter.update(conference.getAllInvitees());
        }
    }
    //endregion
}
