package com.voiceconf.voiceconf.ui.view;

import android.view.View;

import com.parse.ParseUser;

import com.voiceconf.voiceconf.storage.models.Friend;

/**
 * Created by Attila Blenesi on 31 Dec 2015
 */
public class FriendViewHolder extends BaseFriendViewHolder {

    // region CONSTRUCTOR
    public FriendViewHolder(View itemView) {
        super(itemView);
    }
    // endregion

    // region HELPER
    public void setup(Friend friend) {
        if (friend != null) {
            itemView.setTag(friend);
            mAccept.setTag(friend);
            mDecline.setTag(friend);

            if (ParseUser.getCurrentUser().getObjectId().equals(friend.getUser().getObjectId())) {
                setup(friend.getFriend());
                if (friend.isPending()) {
                    mPendingText.setVisibility(View.VISIBLE);
                    mAccept.setVisibility(View.INVISIBLE);
                    mDecline.setVisibility(View.INVISIBLE);
                    return;
                }
            } else {
                setup(friend.getUser());
                if (friend.isPending()) {
                    mPendingText.setVisibility(View.INVISIBLE);
                    mAccept.setVisibility(View.VISIBLE);
                    mDecline.setVisibility(View.VISIBLE);
                    return;
                }
            }

            mPendingText.setVisibility(View.INVISIBLE);
            mAccept.setVisibility(View.INVISIBLE);
            mDecline.setVisibility(View.INVISIBLE);
        }
    }
    // endregion
}