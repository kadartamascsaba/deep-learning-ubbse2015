package com.voiceconf.voiceconf.storage.models;

import com.parse.ParseUser;

/**
 * Created by Attila Blenesi on 26 Dec 2015
 */
public class User extends ParseUser {

    public static final String USER_NAME = "username";//  - The users public full name
    public static final String EMAIL = "email"; //        - The users email address
    public static final String AVATAR = "avatar"; //      - String Url to user profile image
    public static final String USER_DATA = "userData"; // - Contains a JSON example:
    /**
     * {
     * "GoogleId" : "<The users Google Id>",
     * "FacebookId" : "<The users Facebook Id>"
     * }
     */

    public User() {
        super();
    }

    public static void setUserData(ParseUser parseUser, String authData) {
        parseUser.put(USER_DATA, authData);
    }

    public static String getAvatar(ParseUser parseUser) {
        return parseUser.getString(AVATAR);
    }

    public static void setAvatar(ParseUser parseUser, String avatar) {
        parseUser.put(AVATAR, avatar);
    }
}
