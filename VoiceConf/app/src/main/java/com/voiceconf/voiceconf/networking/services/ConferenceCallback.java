package com.voiceconf.voiceconf.networking.services;

import com.voiceconf.voiceconf.storage.models.Conference;

/**
 * Created by Attila Blenesi on 02 Jan 2016
 */
public interface ConferenceCallback {

    void onSuccess(String message, Conference conference);

    void onFailure(Exception e, String message);
}
