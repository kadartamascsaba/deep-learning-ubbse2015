/**
 * Copyright 2015 Halcyon Mobile
 * http://www.halcyonmobile.com
 * All rights reserved.
 */
package com.voiceconf.voiceconf.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.ui.main.friends.FriendsFragment;
import com.voiceconf.voiceconf.ui.main.history.HistoryFragment;

/**
 * Handles the main screens tab layout, fragment instantiation.
 * <p/>
 * Created by Attila Blenesi on 25 Dec 2015
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    //region CONSTANTS
    private static final int ITEM_COUNT = 2;
    private static final int HISTORY_TAB = 0;
    private static final int FRIENDS_TAB = 1;
    //endregion

    //region VARIABLES
    private Context mContext; // used for tab name string resources
    //endregion

    //region CONSTRUCTOR
    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }
    //endregion

    //region ADAPTER METHODS
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case HISTORY_TAB:
                return new HistoryFragment();
            case FRIENDS_TAB:
                return new FriendsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case HISTORY_TAB:
                return mContext.getString(R.string.tab_name_history);
            case FRIENDS_TAB:
                return mContext.getString(R.string.tab_name_friends);
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }
    //endregion
}
