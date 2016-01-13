package com.voiceconf.voiceconf.networking.services;

import android.content.Intent;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.voiceconf.voiceconf.storage.models.User;
import com.voiceconf.voiceconf.ui.main.MainActivity;

/**
 * Created by Zoltán Benedek on 12/30/2015.
 */
public class LoginService {
    private static final String AUTH_DATA_END = "\"}";
    private static final String DEFAULT_PASSWORD = "voiceConf";

    public static void login(final LoginCallback callback, final String name, final String uri, final String id, final String accId, final String email) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo(User.EMAIL, email);
        userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser == null) {
                    // User does not exists => creating new user
                    User user = new User();
                    user.setUsername(name);
                    user.setEmail(email);
                    user.setPassword(DEFAULT_PASSWORD); // This field must be set to create a user

                    // Start sign up request to parse.com
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                User registeredUser = User.createWithoutData(User.class, User.getCurrentUser().getObjectId());
                                User.setAvatar(registeredUser, uri);
                                User.setUserData(registeredUser, id + accId + AUTH_DATA_END);

                                registeredUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            // Creating user was successful starting the main activity
                                            callback.onSucces();
                                        } else {
                                            callback.onFailure(e,e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                callback.onFailure(e,e.getMessage());
                            }
                        }
                    });
                } else {
                    // User exists => Start Log In request to parse.com
                    ParseUser.logInInBackground(name, DEFAULT_PASSWORD, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null) {
                                // Existing user logged in successfully starting the main activity
                                callback.onSucces();
                            } else {
                               callback.onFailure(e,e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
}