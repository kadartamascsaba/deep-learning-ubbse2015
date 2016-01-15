package com.voiceconf.voiceconf.ui.conference;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.networking.services.ConferenceService;
import com.voiceconf.voiceconf.storage.models.Conference;
import com.voiceconf.voiceconf.storage.models.User;
import com.voiceconf.voiceconf.storage.nonpersistent.DataManager;
import com.voiceconf.voiceconf.storage.nonpersistent.SharedPreferenceManager;
import com.voiceconf.voiceconf.storage.nonpersistent.VoiceConfApplication;
import com.voiceconf.voiceconf.ui.main.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Attila Blenesi on 20 Dec 2015
 */
public class ConferenceActivity extends AppCompatActivity implements Observer {

    //region CONSTANTS
    private static final String TAG = "ConferenceActivity";
    private static final String CONFERENCE_ID = "conference_id";
    //endregion

    //region VARIABLES
    private InviteeAdapter mAdapter;
    private CircleImageView mSpeakerAvatar;
    private TextView mSpeakerName;
    private Conference mConference;
    private TextView mSpeakerStarted;
    private TextView mSpeakerDuration;
    private ImageButton mMute;
    private FloatingActionButton startButton, stopButton;
    private boolean mute;
    //endregion

    //region LIFE CYCLE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This action will mute or un-mute your microphone.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mConference = VoiceConfApplication.sDataManager.getConference(getIntent().getStringExtra(CONFERENCE_ID));

        mAdapter = new InviteeAdapter(false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.invitees_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mAdapter);
        mSpeakerAvatar = (CircleImageView) findViewById(R.id.speaker_avatar);
        mSpeakerName = (TextView) findViewById(R.id.speaker_name);

        mSpeakerStarted = (TextView) findViewById(R.id.conference_started);
        mSpeakerDuration = (TextView) findViewById(R.id.conference_duration);

        //Toolbar setup
        final View.OnClickListener underDevelopment = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mSpeakerAvatar, R.string.under_development, Snackbar.LENGTH_LONG).show();
            }
        };
        findViewById(R.id.conference_invite_more).setOnClickListener(underDevelopment);
        findViewById(R.id.conference_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConferenceService.getConferences(null);
                updateScreen();
            }
        });

        mute = false;
        mMute = (ImageButton) findViewById(R.id.conference_mute);
        mMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mute  = !mute;
                AudioManager myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                myAudioManager.setMicrophoneMute(mute);
                if(mute){
                    mMute.setImageResource(R.drawable.ic_volume_up_white_24dp);
                }
                else{
                    mMute.setImageResource(R.drawable.ic_volume_off_white_24dp);
                }
            }
        });
        findViewById(R.id.conference_settings).setOnClickListener(underDevelopment);

        findViewById(R.id.conference_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                closeConference();
            }
        });

        startButton = (FloatingActionButton) findViewById(R.id.fab);
        stopButton = (FloatingActionButton) findViewById(R.id.conference_close);
        startButton.setOnClickListener(startListener);
        stopButton.setOnClickListener(stopListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateScreen();
        VoiceConfApplication.sDataManager.addObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoiceConfApplication.sDataManager.deleteObserver(this);

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
    //endregion

    public static Intent getStartIntent(Context context, Conference conference) {
        Intent intent = new Intent(context, ConferenceActivity.class);
        intent.putExtra(CONFERENCE_ID, conference.getObjectId());
        return intent;
    }

    private void closeConference() {
        if (ParseUser.getCurrentUser().getObjectId().equals(mConference.getOwner().getObjectId())) {
            mConference.setClosed(true);
            mConference.saveInBackground();
        }
        finish();
    }

    private void updateScreen() {
        mConference = VoiceConfApplication.sDataManager.getConference(getIntent().getStringExtra(CONFERENCE_ID));
        if (mConference != null) {
            ((TextView) findViewById(R.id.conference_title)).setText(mConference.getTitle());
            ((TextView) findViewById(R.id.conference_date)).setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(mConference.getCreatedAt()));
            mSpeakerStarted.setText("Started\n" + new SimpleDateFormat("hh:mm", Locale.getDefault()).format(mConference.getCreatedAt()));

            long minutes = TimeUnit.MILLISECONDS.toMinutes((mConference.isClosed() ? mConference.getUpdatedAt().getTime() : System.currentTimeMillis()) - mConference.getCreatedAt().getTime());
            mSpeakerDuration.setText("Duration\n" + minutes / 60 + " h " + minutes % 60 + " min");

            mAdapter.update(mConference.getInvitees());

            if (mConference.isClosed()) {
                ((TextView) findViewById(R.id.speaker_title)).setText(R.string.owner);

                findViewById(R.id.conference_toolbar).setVisibility(View.GONE);
                findViewById(R.id.conference_close).setVisibility(View.GONE);
                findViewById(R.id.fab).setVisibility(View.GONE);

                Glide.with(this).load(User.getAvatar(mConference.getOwner())).into(mSpeakerAvatar);
                mSpeakerName.setText(mConference.getOwner().getUsername());

            } else {
                ParseUser user = ParseUser.getCurrentUser();
                Glide.with(this).load(User.getAvatar(user)).into(mSpeakerAvatar);
                mSpeakerName.setText(user.getUsername());
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if ((int) data == DataManager.CONFERENCE_UPDATED) {
            updateScreen();
        }
    }

    // region COMMUNICATION
    private static DatagramSocket socketOut;
    private static DatagramSocket socketIn;

    private String ipAddress = SharedPreferenceManager.getInstance(this).getSavedIpAddress();
    private int serverPort = Integer.parseInt(SharedPreferenceManager.getInstance(this).getSavedPort());
    private int clientPort = 56789;

    private int sampleRate = 16000;
    private int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int minBufSize = 4096;

    private static boolean status = false;

    private AudioRecord recorder = null;
    private AudioTrack track = null;

    private final View.OnClickListener stopListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            status = false;
            recorder.release();
        }
    };
    private final View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            status = true;
            startStreaming();
        }
    };

    private void startStreaming() {
        recordSound();
        playSound();
    }

    private void recordSound() {

        Thread recordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketOut = new DatagramSocket();
                    byte[] buffer = new byte[minBufSize];

                    DatagramPacket packet;
                    final InetAddress destination = InetAddress.getByName(ipAddress);

                    recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, minBufSize);
                    recorder.startRecording();

                    while (status == true) {
                        minBufSize = recorder.read(buffer, 0, buffer.length);

                        packet = new DatagramPacket(buffer, buffer.length, destination, serverPort);
                        socketOut.send(packet);
                    }
                } catch (UnknownHostException e) {
                    Log.e("VS", "UnknownHostException", e);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("VS", "" + e);
                }
            }
        });
        recordThread.start();
    }

    private void playSound() {

        Thread playThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketIn = new DatagramSocket(clientPort);

                    track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat, minBufSize, AudioTrack.MODE_STREAM);
                    track.play();

                    try {
                        byte[] buf = new byte[minBufSize];

                        while (status == true) {
                            DatagramPacket pack = new DatagramPacket(buf, minBufSize);
                            socketIn.receive(pack);
                            track.write(pack.getData(), 0, pack.getLength());
                        }
                    } catch (SocketException se) {
                        Log.e("", "SocketException: " + se.toString());
                    } catch (IOException ie) {
                        Log.e("", "IOException" + ie.toString());
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        });
        playThread.start();
    }
    // endregion
}