package com.voiceconf.voiceconf.storage.nonpersistent;

import com.voiceconf.voiceconf.storage.models.Friend;

import java.util.List;
import java.util.Observable;

/**
 * Use this class to manage data for the application lifecycle;
 *
 * Created by Attila Blenesi on 28 Dec 2015
 */
public class DataManager extends Observable {

    public static final int FRIENDS_UPDATED = 1; // Data type for observer notification

    private List<Friend> mFriends;

    public List<Friend> getFriends() {
        return mFriends;
    }

    public void setFriends(List<Friend> friends) {
        this.mFriends = friends;
        setChanged();
        notifyObservers(FRIENDS_UPDATED);
    }
}
