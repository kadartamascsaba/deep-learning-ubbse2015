package com.voiceconf.voiceconf.ui.conference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.Conference;
import com.voiceconf.voiceconf.storage.models.Invite;
import com.voiceconf.voiceconf.storage.nonpersistent.VoiceConfApplication;

/**
 * Created by Attila Blenesi on 20 Dec 2015
 */
public class ConferenceActivity extends AppCompatActivity {


    private static final String CONFERENCE_ID = "conference_id";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This action will mute or un-mute your microphone.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Conference conference = VoiceConfApplication.sDataManager.getConference(getIntent().getStringExtra(CONFERENCE_ID));

        InviteeAdapter inviteeAdapter = new InviteeAdapter(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.invitees_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(inviteeAdapter);
        inviteeAdapter.update(conference.getInvitees());
    }

    public static Intent getStartIntent(Context context, Conference conference){
        Intent intent = new Intent(context, ConferenceActivity.class);
        intent.putExtra(CONFERENCE_ID, conference.getObjectId());
        return intent;
    }
}
