package com.voiceconf.voiceconf.ui.conference.setup;

import android.view.View;
import android.widget.ImageView;

import com.parse.ParseUser;
import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.ui.view.BaseFriendViewHolder;

/**
 * Created by Attila Blenesi on 31 Dec 2015
 */
public class SelectFriendViewHolder extends BaseFriendViewHolder{

    //region VARIABLES
    private ImageView mSelected;
    //endregion

    //region CONSTRUCTOR
    public SelectFriendViewHolder(View itemView) {
        super(itemView);
        mSelected = (ImageView) itemView.findViewById(R.id.user_selected);
    }
    //endregion

    //region HELPER
    protected void setup(ParseUser user, boolean selected) {
        super.setup(user);
        if(selected){
            mSelected.setVisibility(View.VISIBLE);
        }else{
            mSelected.setVisibility(View.INVISIBLE);
        }
        setup(user);
    }
    //endregion
}
