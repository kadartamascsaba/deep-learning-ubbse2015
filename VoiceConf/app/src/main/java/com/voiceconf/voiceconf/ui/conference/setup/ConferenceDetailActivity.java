package com.voiceconf.voiceconf.ui.conference.setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.ui.conference.ConferenceActivity;
import com.voiceconf.voiceconf.ui.main.MainActivity;

/**
 * This activity provides the interface for the user to complete the necessary information,
 * for starting a conference: Conference title (group title) and invitees.
 * <p/>
 * Created by Attila Blenesi on 20 Dec 2015
 */
public class ConferenceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_detail);

        // Toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Floating Action Button setup
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On F.A.B. press the conference will be started.
                startActivity(new Intent(ConferenceDetailActivity.this, ConferenceActivity.class));
            }
        });

        findViewById(R.id.add_invitees).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ConferenceDetailActivity.this, SelectFriendsActivity.class), SelectFriendsActivity.RESULT_CODE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling the up navigation
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelectFriendsActivity.RESULT_CODE && resultCode == RESULT_OK) {
            
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
