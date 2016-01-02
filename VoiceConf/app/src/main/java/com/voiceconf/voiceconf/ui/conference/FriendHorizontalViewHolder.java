package com.voiceconf.voiceconf.ui.conference;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.Invite;
import com.voiceconf.voiceconf.storage.models.User;

/**
 * Created by Attila Blenesi on 02 Jan 2016
 */
public class FriendHorizontalViewHolder extends RecyclerView.ViewHolder {

    private TextView mName;
    private ImageView mAvatarStyle;

    public FriendHorizontalViewHolder(View view) {
        super(view);
        mName = (TextView) view.findViewById(R.id.user_name);
        mAvatarStyle = (ImageView) view.findViewById(R.id.user_avatar_style);
    }

    public void setup(Invite invite) {
        itemView.setTag(invite);
        if(invite!=null){
            Glide.with(itemView.getContext()).load(User.getAvatar(invite.getInvited())).into((ImageView) itemView.findViewById(R.id.user_avatar));
            if (invite.getInvited()!= null && !TextUtils.isEmpty(invite.getInvited().getUsername())) {
                mName.setText(invite.getInvited().getUsername());
            }
//            if(invite.isAccepted()){
//                mAvatarStyle.setVisibility(View.VISIBLE);
//                mAvatarStyle.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.bg_selected));
//            }else{
//                mAvatarStyle.setVisibility(View.GONE);
//                mAvatarStyle.setImageDrawable(null);
//            }
        }
    }
}
