package com.voiceconf.voiceconf.storage;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.voiceconf.voiceconf.storage.models.User;

/**
 * This call will be persisted trough the applications lifecycle,
 * store only the most reused data here.
 *
 * Created by Attila Blenesi on 25 Dec 2015
 */
public class VoiceConfApplication extends Application {

    /**
     * This method is called first, when the application starts.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Parse initialization
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        // Registering the ParseObject subclasses
        ParseObject.registerSubclass(User.class);

    }
}
