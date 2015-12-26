/**
 * Copyright 2015 Halcyon Mobile
 * http://www.halcyonmobile.com
 * All rights reserved.
 */
package com.voiceconf.voiceconf.storage.models;

import com.parse.ParseUser;

/**
 * Created by Attila Blenesi on 26 Dec 2015
 */
public class User extends ParseUser {

    // username - The users public full name
    // email    - The users email address
    private static final String AVATAR = "avatar"; // - string Url to user profile image
    private static final String USER_DATA = "userData"; // - Contains a JSON example:
    /**
     * {
     * "GoogleId" : "<The users Google Id>",
     * "FacebookId" : "<The users Facebook Id>"
     * }
     */

    public User(){
        super();
    }

    public void setUserData(String authData) {
        put(USER_DATA, authData);
    }

    public String getAvatar() {
        return getString(AVATAR);
    }

    public void setAvatar(String avatar) {
        put(AVATAR, avatar);
    }
}
