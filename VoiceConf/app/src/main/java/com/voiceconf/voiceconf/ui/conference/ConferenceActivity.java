package com.voiceconf.voiceconf.ui.conference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
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
    private static final String CONFERENCE_ID = "conference_id";
    private static final String STOP = "stop";
    private static final String START = "start";
    //endregion

    //region VARIABLES
    private InviteeAdapter mAdapter;
    private CircleImageView mSpeakerAvatar;
    private TextView mSpeakerName;
    private Conference mConference;
    private TextView mSpeakerStarted;
    private TextView mSpeakerDuration;
    private FloatingActionButton mStartConferenceButton;
    private boolean isMicrophoneMuted;

    //region COMMUNICATION RELATED VARIABLES

    //region CONSTANTS
    private static final int clientPort = 56788; // Sound receiving socket
    private static final int clientDataPort = 56798; // Data receiving socket

    private static final int SAMPLE_RATE = 16000;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    //endregion

    //region VARIABLES
    private static boolean communicationStatus = false;
    private static DatagramSocket mSocketIn;
    private static DatagramSocket mSocketOut;
    private InetAddress destination;
    private AudioRecord mRecorder = null;
    private AudioTrack mTrack = null;
    private int minBufSize = 4096;
    private int serverPort;
    //endregion

    //endregion

    //region LIFE CYCLE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        final FloatingActionButton muteMicFab = (FloatingActionButton) findViewById(R.id.fab);
        muteMicFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMicrophoneMuted = !isMicrophoneMuted;
                if (isMicrophoneMuted) {
                    muteMicFab.setImageDrawable(ContextCompat.getDrawable(ConferenceActivity.this, R.drawable.ic_mic_white_24dp));
                } else {
                    muteMicFab.setImageDrawable(ContextCompat.getDrawable(ConferenceActivity.this, R.drawable.ic_mic_off_white_24dp));
                }
            }
        });

        // Get current conference from intent
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
        View.OnClickListener underDevelopment = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mSpeakerAvatar, R.string.under_development, Snackbar.LENGTH_LONG).show();
            }
        };

        // Invite more people
        findViewById(R.id.conference_invite_more).setOnClickListener(underDevelopment);

        // Synchronise with database
        findViewById(R.id.conference_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConferenceService.getConferences(null);
                updateScreen(false);
            }
        });

        ImageButton muteSpeaker = (ImageButton) findViewById(R.id.conference_mute);
        muteSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.conference_settings).setOnClickListener(underDevelopment);

        // Handle conference start an stop
        mStartConferenceButton = (FloatingActionButton) findViewById(R.id.conference_start);
        final FloatingActionButton stopConferenceButton = (FloatingActionButton) findViewById(R.id.conference_close);
        mStartConferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                communicationStatus = true;
                recordSound();
                playSound();
                getConferenceData();
                mStartConferenceButton.setVisibility(View.GONE);
                stopConferenceButton.setVisibility(View.VISIBLE);
            }
        });
        stopConferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                communicationStatus = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new DatagramSocket().send(new DatagramPacket(STOP.getBytes(), STOP.getBytes().length, destination, serverPort));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                mRecorder.release();
                mTrack.release();

                // If the current user is the owner the conference will be closed
                if (ParseUser.getCurrentUser().getObjectId().equals(mConference.getOwner().getObjectId())) {
                    mConference.setClosed(true);
                    mConference.saveInBackground();
                }
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        serverPort = SharedPreferenceManager.getInstance(getApplicationContext()).getSavedPort();
        String ipAddress = SharedPreferenceManager.getInstance(getApplicationContext()).getSavedIpAddress();
        try {
            destination = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        updateScreen(false);
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

    @SuppressLint("SetTextI18n")
    private void updateScreen(boolean quick) {
        mConference = VoiceConfApplication.sDataManager.getConference(getIntent().getStringExtra(CONFERENCE_ID));
        if (mConference != null) {
            ((TextView) findViewById(R.id.conference_title)).setText(mConference.getTitle());
            ((TextView) findViewById(R.id.conference_date)).setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(mConference.getCreatedAt()));
            mSpeakerStarted.setText("Started\n" + new SimpleDateFormat("hh:mm", Locale.getDefault()).format(mConference.getCreatedAt()));

            long minutes = TimeUnit.MILLISECONDS.toMinutes((mConference.isClosed() ? mConference.getUpdatedAt().getTime() : System.currentTimeMillis()) - mConference.getCreatedAt().getTime());
            mSpeakerDuration.setText("Duration\n" + minutes / 60 + " h " + minutes % 60 + " min");

            mAdapter.update(mConference.getInvitees());

            if (!quick) {
                if (mConference.isClosed()) {
                    ((TextView) findViewById(R.id.speaker_title)).setText(R.string.owner);
                    mStartConferenceButton.setVisibility(View.GONE);
                    findViewById(R.id.conference_toolbar).setVisibility(View.GONE);
                    findViewById(R.id.conference_close).setVisibility(View.GONE);
                    findViewById(R.id.fab).setVisibility(View.GONE);

                    Glide.with(this).load(User.getAvatar(mConference.getOwner())).into(mSpeakerAvatar);
                    mSpeakerName.setText(mConference.getOwner().getUsername());
                } else {
                    mSpeakerName.setText("Unknown speaker");
                }
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if ((int) data == DataManager.CONFERENCE_UPDATED) {
            updateScreen(false);
        }
    }

    // region COMMUNICATION
    private void recordSound() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocketOut = new DatagramSocket();
                    byte[] buffer = new byte[minBufSize];

                    DatagramPacket packet;
                    packet = new DatagramPacket(START.getBytes(), START.getBytes().length, destination, serverPort);
                    mSocketOut.send(packet);
                    mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, minBufSize);
                    mRecorder.startRecording();

                    while (communicationStatus) {
                        minBufSize = mRecorder.read(buffer, 0, buffer.length);

                        packet = new DatagramPacket(buffer, buffer.length, destination, serverPort);
                        mSocketOut.send(packet);
                    }

                } catch (UnknownHostException e) {
                    Log.e("VS", "UnknownHostException", e);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("VS", "" + e);
                }
            }
        }).start();
    }

    private void playSound() {
        Thread playThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocketIn = new DatagramSocket(clientPort);
                    mTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, minBufSize, AudioTrack.MODE_STREAM);
                    mTrack.play();

                    try {
                        byte[] buf = new byte[minBufSize];
                        while (communicationStatus) {
                            DatagramPacket pack = new DatagramPacket(buf, minBufSize);
                            mSocketIn.receive(pack);
                            Log.d(TAG, "SOUND: " + new String(pack.getData(), 0, pack.getLength()));
                            mTrack.write(pack.getData(), 0, pack.getLength());
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

    private String currentSpeakerId;
    private String currentSpeakerIdOld;

    private void getConferenceData() {
        final Handler handler = new Handler();
        Thread playThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    currentSpeakerIdOld = "unknown";
                    mSocketIn = new DatagramSocket(clientDataPort);
                    try {
                        byte[] buf = new byte[minBufSize];
                        while (communicationStatus) {
                            DatagramPacket pack = new DatagramPacket(buf, minBufSize);
                            mSocketIn.receive(pack);

                            currentSpeakerId = new String(pack.getData(), 0, pack.getLength());

                            if(!currentSpeakerId.equals(currentSpeakerIdOld)) {
                                currentSpeakerIdOld = currentSpeakerId;
                                handler.post(
                                        new Runnable() {
                                    @Override
                                    public void run() {
                                        updateCurrentSpeaker(currentSpeakerId);
                                    }
                                });
                            }
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

    private static final String TAG = "ConferenceActivity";

    private void updateCurrentSpeaker(String user) {
        Log.d(TAG, "DATA: " + user);
        switch (user) {
            case "unknown":
                mSpeakerName.setText("Unknown speaker");
                mSpeakerAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder_conference));
                break;
            case "tamas":
                mSpeakerName.setText("Tamás Csaba Kádár");
                Glide.with(this).load("https://lh5.googleusercontent.com/-UjNDPDLWmyE/AAAAAAAAAAI/AAAAAAAAABA/rIM27ux4reY/photo.jpg").into(mSpeakerAvatar);
                break;
            case "nandi":
                mSpeakerName.setText("Nándor Kedves");
                Glide.with(this).load("https://scontent-frt3-1.xx.fbcdn.net/hphotos-xpt1/v/t1.0-9/12509720_1117195188324605_3150122052569060595_n.jpg?oh=277336556d493082314230651b43f74a&oe=570A079F").into(mSpeakerAvatar);
                break;
            case "beni":
                mSpeakerName.setText("Zoltán Benedek");
                Glide.with(this).load("https://lh5.googleusercontent.com/-umTQuou3YuY/AAAAAAAAAAI/AAAAAAAAAOQ/lcmE3d7jHnA/photo.jpg").into(mSpeakerAvatar);
                break;
            case "katinka":
                mSpeakerName.setText("Katinka Páll");
                Glide.with(this).load("https://lh3.googleusercontent.com/-3JQ5-II2tG4/AAAAAAAAAAI/AAAAAAAAADY/HBvfA40BgFo/photo.jpg").into(mSpeakerAvatar);
                break;
            case "battila":
                mSpeakerName.setText("Attila Blénesi");
                Glide.with(this).load("https://lh5.googleusercontent.com/-TVP7AB8Pplg/AAAAAAAAAAI/AAAAAAAAEZs/s_k6RDECI04/photo.jpg").into(mSpeakerAvatar);
                break;
            default:
                mSpeakerName.setText("Unknown speaker");
                mSpeakerAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder_conference));
                break;
        }
    }
    //endregion
}