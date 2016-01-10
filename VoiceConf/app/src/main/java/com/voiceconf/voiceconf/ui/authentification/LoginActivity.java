package com.voiceconf.voiceconf.ui.authentification;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.models.User;
import com.voiceconf.voiceconf.ui.main.MainActivity;


/**
 * This activity handles the user login.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final String GOOGLE_USER_ID = "{\"GoogleUserID\" : \"";
    private static final String AUTH_DATA_END = "\"}";
    private static final String DEFAULT_PASSWORD = "voiceConf";
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setScopes(gso.getScopeArray());
        mSignInButton.setSize(SignInButton.SIZE_WIDE);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Snackbar.make(mSignInButton, R.string.login_failed, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e(TAG, "onActivityResult: " + result.getStatus());
            if (result.isSuccess()) {
                final GoogleSignInAccount acct = result.getSignInAccount();
                // Request was successful => checking if the user exists
                ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                userQuery.whereEqualTo(User.EMAIL, acct.getEmail());
                userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser == null) {
                            // User does not exists => creating new user
                            User user = new User();
                            user.setUsername(acct.getDisplayName());
                            user.setEmail(acct.getEmail());
                            user.setPassword(DEFAULT_PASSWORD); // This field must be set to create a user

                            // Start sign up request to parse.com
                            user.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        User registeredUser = User.createWithoutData(User.class, User.getCurrentUser().getObjectId());
                                        User.setAvatar(registeredUser, acct.getPhotoUrl().toString());
                                        User.setUserData(registeredUser, GOOGLE_USER_ID + acct.getId() + AUTH_DATA_END);

                                        registeredUser.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    // Creating user was successful starting the main activity
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    finish();
                                                } else {
                                                    somethingWentWrong();
                                                }
                                            }
                                        });
                                    } else {
                                        somethingWentWrong();
                                    }
                                }
                            });
                        } else {
                            // User exists => Start Log In request to parse.com
                            ParseUser.logInInBackground(acct.getDisplayName(), DEFAULT_PASSWORD, new LogInCallback() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if (e == null) {
                                        // Existing user logged in successfully starting the main activity
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        somethingWentWrong();
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                somethingWentWrong();
            }
        }
    }

    /**
     * Something went wrong notifying the user
     */
    private void somethingWentWrong() {
        Snackbar.make(mSignInButton, R.string.something_went_wrong, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSignInButton.performClick();
                    }
                })
                .show();
    }
}
