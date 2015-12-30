/**
 * Copyright 2015 Halcyon Mobile
 * http://www.halcyonmobile.com
 * All rights reserved.
 */
package com.voiceconf.voiceconf.storage.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Helper class to manage friends.
 *
 * Created by Attila Blenesi on 28 Dec 2015
 */
@ParseClassName("Friend")
public class Friend extends ParseObject{

    //region CONSTANTS
    private static final String USER_ID = "userId";
    private static final String FRIEND_ID = "friendId";
    private static final String PENDING = "pending";
    //endregion

    //region GETTERS / SETTERS
    public ParseUser getUser() {
        return getParseUser(USER_ID);
    }

    public void setUser(String userId) {
        put(USER_ID, ParseUser.createWithoutData(ParseUser.class, userId));
    }

    public ParseUser getFriend() {
        return getParseUser(FRIEND_ID);
    }

    public void setFriend(String friendId) {
        put(FRIEND_ID, ParseUser.createWithoutData(ParseUser.class, friendId));
    }

    public boolean isPending() {
        return getBoolean(PENDING);
    }

    public void setPending(boolean pending) {
        put(PENDING, pending);
    }
    //endregion
}
