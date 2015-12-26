package com.voiceconf.voiceconf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.parse.ParseUser;
import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.ui.authentification.LoginActivity;
import com.voiceconf.voiceconf.ui.main.MainActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if the user is signed in we will start the main activity else the login activity
        startActivity(new Intent(LandingActivity.this, ParseUser.getCurrentUser() == null ? LoginActivity.class : MainActivity.class));
        finish();
    }

}
