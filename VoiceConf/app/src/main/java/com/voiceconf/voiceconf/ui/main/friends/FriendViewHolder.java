package com.voiceconf.voiceconf.ui.main.friends;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseUser;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.Friend;
import com.voiceconf.voiceconf.ui.view.BaseFriendViewHolder;

/**
 * Created by Attila Blenesi on 31 Dec 2015
 */
public class FriendViewHolder extends BaseFriendViewHolder {

    //region VARIABLES
    private ImageButton mAccept;
    private ImageButton mDecline;
    private TextView mPendingText;
    //endregion

    // region CONSTRUCTOR
    public FriendViewHolder(View itemView) {
        super(itemView);
        mPendingText = (TextView) itemView.findViewById(R.id.pending);
        mAccept = (ImageButton) itemView.findViewById(R.id.accept);
        mDecline = (ImageButton) itemView.findViewById(R.id.decline);
    }
    // endregion

    // region HELPER
    public void setup(Friend friend) {
        if (friend != null) {
            itemView.setTag(friend);
            mAccept.setTag(friend);
            mDecline.setTag(friend);

            // Select the correct user to be displayed
            if (ParseUser.getCurrentUser() != null && friend.getUser() != null && ParseUser.getCurrentUser().getObjectId().equals(friend.getUser().getObjectId())) {
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