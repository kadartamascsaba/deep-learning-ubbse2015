package com.voiceconf.voiceconf.networking.services;

/**
 * Created by beniii on 1/4/2016.
 */
public interface LoginCallback {

    void onSucces();

    void onFailure(Exception e, String message);
}
