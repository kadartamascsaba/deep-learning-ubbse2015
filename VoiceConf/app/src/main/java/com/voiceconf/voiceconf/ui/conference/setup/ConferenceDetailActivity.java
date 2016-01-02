package com.voiceconf.voiceconf.ui.conference.setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.networking.services.ConferenceCallback;
import com.voiceconf.voiceconf.networking.services.ConferenceService;
import com.voiceconf.voiceconf.storage.models.Conference;
import com.voiceconf.voiceconf.storage.nonpersistent.VoiceConfApplication;
import com.voiceconf.voiceconf.ui.conference.ConferenceActivity;
import com.voiceconf.voiceconf.ui.conference.setup.select_friends.SelectFriendsActivity;
import com.voiceconf.voiceconf.ui.main.MainActivity;
import com.voiceconf.voiceconf.ui.view.PlaceholderRecyclerView;

/**
 * This activity provides the interface for the user to complete the necessary information,
 * for starting a conference: Conference title (group title) and invitees.
 * <p/>
 * Created by Attila Blenesi on 20 Dec 2015
 */
public class ConferenceDetailActivity extends AppCompatActivity {

    //region VARIABLES
    private InviteesAdapter mInviteesAdapter;
    private PlaceholderRecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    //endregion

    //region LIFE CYCLE METHODS
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

        final EditText editTitle = (EditText) findViewById(R.id.conference_title);
        // Floating Action Button setup
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On F.A.B. press the conference will be saved then started.
                mFloatingActionButton.setEnabled(false);
                ConferenceService.saveConferenceWithInvites(new ConferenceCallback() {
                    @Override
                    public void onSuccess(String message, Conference conference) {
                        Snackbar.make(mFloatingActionButton, message, Snackbar.LENGTH_LONG).show();
                        if(conference != null){
                            NavUtils.navigateUpTo(ConferenceDetailActivity.this, new Intent(ConferenceDetailActivity.this, MainActivity.class));
                            startActivity(ConferenceActivity.getStartIntent(ConferenceDetailActivity.this, conference));
                        }
                    }

                    @Override
                    public void onFailure(Exception e, String message) {
                        Snackbar.make(mFloatingActionButton, message, Snackbar.LENGTH_LONG).show();
                        mFloatingActionButton.setEnabled(true);
                    }
                }, editTitle.getText().toString(), mInviteesAdapter.getItemIds());
            }
        });

        // Add invitees with placeholder
        View placeholder = findViewById(R.id.add_invitees);
        placeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ConferenceDetailActivity.this, SelectFriendsActivity.class), SelectFriendsActivity.RESULT_CODE);
            }
        });

        // Recycler view setup
        mInviteesAdapter = new InviteesAdapter(null);
        mRecyclerView = (PlaceholderRecyclerView) findViewById(R.id.invitees_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mInviteesAdapter);
        mRecyclerView.setEmptyView(placeholder);
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
            mInviteesAdapter.update(VoiceConfApplication.sDataManager.getUsers(data.getStringArrayListExtra(SelectFriendsActivity.SELECTED_USER_IDS)));
            mRecyclerView.updateEmptyView(true);
            mRecyclerView.setVisibility(View.VISIBLE);
            mFloatingActionButton.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //endregion
}
