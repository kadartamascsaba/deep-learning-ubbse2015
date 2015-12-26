package com.voiceconf.voiceconf.storage.models;

import android.app.Application;

/**
 * This call will be persisted trough the applications lifecycle,
 * store only the most reused data here.
 *
 * Created by Attila Blenesi on 25 Dec 2015
 */
public class VoiceConfApplication extends Application {

    /**
     * This method is called first,when the application starts.
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
