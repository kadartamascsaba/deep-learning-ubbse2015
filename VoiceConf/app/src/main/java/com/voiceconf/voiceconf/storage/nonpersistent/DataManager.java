package com.voiceconf.voiceconf.storage.nonpersistent;

import com.parse.ParseUser;
import com.voiceconf.voiceconf.storage.models.Friend;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Use this class to manage data for the application lifecycle;
 * <p/>
 * Created by Attila Blenesi on 28 Dec 2015
 */
public class DataManager extends Observable {

    public static final int FRIENDS_UPDATED = 1; // Data type for observer notification

    private List<Friend> mFriends;

    public List<Friend> getFriends() {
        return mFriends;
    }

    /**
     * Use this method to get only ParseUser friend objects.
     *
     * @return A list of friends as ParseUser
     */
    public List<ParseUser> getUsers() {
        List<ParseUser> friends = new ArrayList<>();
        for (Friend friend : mFriends) {
            if (!friend.isPending()) {
                if (ParseUser.getCurrentUser().getObjectId().equals(friend.getUser().getObjectId())) {
                    friends.add(friend.getFriend());
                } else {
                    friends.add(friend.getUser());
                }
            }
        }
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.mFriends = friends;
        setChanged();
        notifyObservers(FRIENDS_UPDATED);
    }
}
