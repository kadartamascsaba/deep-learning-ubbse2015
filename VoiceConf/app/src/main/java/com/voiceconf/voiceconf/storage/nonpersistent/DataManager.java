package com.voiceconf.voiceconf.storage.nonpersistent;

import android.support.annotation.NonNull;

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

    //region VARIABLES
    public static final int FRIENDS_UPDATED = 1; // Data type for observer notification

    private List<Friend> mFriends;
    //endregion

    //region GETTER/SETTERS

    /**
     * Sets the friend list and notifies the proper observers.
     *
     * @param friends The list of friend that will be stored.
     */
    public void setFriends(List<Friend> friends) {
        this.mFriends = friends;
        setChanged();
        notifyObservers(FRIENDS_UPDATED);
    }

    public List<Friend> getFriends() {
        return mFriends;
    }
    //endregion

    //region HELPER METHODS

    /**
     * Use this method to get only ParseUser friend objects.
     *
     * @return A list of friends as ParseUser
     */
    public List<ParseUser> getUsers() {
        if (mFriends != null) {
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
        } else {
            return null;
        }
    }


    /**
     * Helper method to get the user given a list of ids.
     *
     * @param stringArrayListExtra The list of user ids needed to be returned.
     * @return List of requested users.
     */
    public List<ParseUser> getUsers(@NonNull ArrayList<String> stringArrayListExtra) {
        if (mFriends != null) {
            List<ParseUser> friends = new ArrayList<>();
            for (Friend friend : mFriends) {
                if (!friend.isPending()) {
                    for (String id : stringArrayListExtra) {
                        if (friend.getUser().getObjectId().equals(id)) {
                            friends.add(friend.getUser());
                        } else {
                            if (friend.getFriend().getObjectId().equals(id)) {
                                friends.add(friend.getFriend());
                            }
                        }
                    }
                }
            }
            return friends;
        } else {
            return null;
        }
    }
    //endregion
}
