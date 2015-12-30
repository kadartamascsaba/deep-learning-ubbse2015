package com.voiceconf.voiceconf.networking.services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.voiceconf.voiceconf.storage.models.Friend;
import com.voiceconf.voiceconf.storage.models.User;

/**
 * Created by Attila Blenesi on 27 Dec 2015
 */
public class FriendService {

    /**
     * User this metod to add a new friend via email.
     * @param context Needed for toast to notify the user.
     * @param email The email address for the friend
     */
    public static void addNewFriend(@NonNull final Context context, @NonNull String email) {
        if(email.equals(ParseUser.getCurrentUser().getEmail())){
            Toast.makeText(context, "You cant add yourself as friend", Toast.LENGTH_LONG).show();
        }
        final Friend friend = new Friend();
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo(User.EMAIL, email);
        userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    friend.setFriendId(parseUser.getObjectId());
                    friend.setUserId(User.getCurrentUser().getObjectId());
                    friend.setPending(true);
                    friend.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                                Toast.makeText(context, "Friend added succesfully", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
