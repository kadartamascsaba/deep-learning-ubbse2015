package com.voiceconf.voiceconf.networking.services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.voiceconf.voiceconf.storage.models.Conference;
import com.voiceconf.voiceconf.storage.models.Invite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attila Blenesi on 01 Jan 2016
 */
public class ConferenceService {

    private static final String TAG = "ConferenceService";

    public static void saveConferenceWithInvites(@NonNull final Context context, @NonNull String title, @NonNull final List<String> inviteeIds) {
        final List<Invite> invites = new ArrayList<>();
        final Conference conference = new Conference();
        conference.setOwner();
        conference.setTitle(title);

        conference.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Creating invites
                    for (String id : inviteeIds) {
                        Invite invite = new Invite();
                        Log.d(TAG, "done: " + conference.getObjectId());
                        invite.setConference(conference.getObjectId());
                        invite.setInvited(id);
                        invites.add(invite);
                    }
                    ParseObject.saveAllInBackground(invites, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                conference.putInvites(invites);
                                conference.saveInBackground();
                                Toast.makeText(context, "Invitees where notified successfully.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Something went wrong while sending invites.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(context, "Conference created successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Something went wrong while creating the conference.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
