package com.voiceconf.voiceconf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;
import com.voiceconf.voiceconf.storage.models.User;
import com.voiceconf.voiceconf.ui.authentification.LoginActivity;
import com.voiceconf.voiceconf.ui.main.MainActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if the user is signed in we will start the main activity else the login activity
        ParseUser user = User.getCurrentUser();
        startActivity(new Intent(LandingActivity.this, user == null ? LoginActivity.class : MainActivity.class));
        finish();
    }

}
