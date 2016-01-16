package com.voiceconf.voiceconf.storage.nonpersistent;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tamas-Csaba Kadar on 1/2/2016.
 */
public class SharedPreferenceManager {

    private final static String PREF_NAME = "voiceconf_prefs";
    private final static String KEY_IP = "ipaddress";
    private final static String KEY_PORT = "port";

    public static SharedPreferenceManager sInstance = null;
    private SharedPreferences mSharedPrefs = null;

    public SharedPreferenceManager(Context context) {
        mSharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceManager(context);
        }

        return sInstance;
    }

    public void saveServerData(String ip, int port) {
        SharedPreferences.Editor editor = this.mSharedPrefs.edit();
        editor.putString(KEY_IP, ip);
        editor.putInt(KEY_PORT, port);
        editor.apply();
    }

    public String getSavedIpAddress() {
        return mSharedPrefs.getString(KEY_IP, "192.168.0.16");
    }

    public int getSavedPort() {
        return mSharedPrefs.getInt(KEY_PORT, 6789);
    }

}
