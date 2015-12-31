package com.voiceconf.voiceconf.ui.view;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.User;

/**
 * Created by Attila Blenesi on 31 Dec 2015
 */
public class BaseFriendViewHolder extends RecyclerView.ViewHolder {

    // region VARIABLES
    protected TextView mFriendName;
    protected TextView mEmail;
    // endregion

    // region CONSTRUCTOR
    public BaseFriendViewHolder(final View itemView) {
        super(itemView);
        mFriendName = (TextView) itemView.findViewById(R.id.user_name);
        mEmail = (TextView) itemView.findViewById(R.id.user_email);
    }
    // endregion

    // region HELPER
    protected void setup(ParseUser user) {
        itemView.setTag(user);
        // Load friend name
        if (user != null) {
            Glide.with(itemView.getContext()).load(User.getAvatar(user)).into((ImageView) itemView.findViewById(R.id.user_avatar));
            if (!TextUtils.isEmpty(user.getUsername())) {
                mFriendName.setText(user.getUsername());
            }
            if (!TextUtils.isEmpty(user.getEmail())) {
                mEmail.setText(user.getEmail());
            }
        }
    }
    // endregion
}
