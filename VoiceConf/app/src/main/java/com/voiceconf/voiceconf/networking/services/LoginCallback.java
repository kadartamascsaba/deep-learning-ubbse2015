package com.voiceconf.voiceconf.networking.services;

/**
 * Created by Zoltan Benedek on 1/4/2016.
 * Edited by Blenesi Attila
 */
public interface LoginCallback {

    void onSucces();

    void onFailure(Exception e);
}
