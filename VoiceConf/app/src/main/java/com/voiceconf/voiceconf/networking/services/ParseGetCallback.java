package com.voiceconf.voiceconf.networking.services;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Attila Blenesi on 30 Dec 2015
 */
public interface ParseGetCallback<T extends ParseObject> {

    void onResult(List<T> result);

    void onFailure(Exception e);
}