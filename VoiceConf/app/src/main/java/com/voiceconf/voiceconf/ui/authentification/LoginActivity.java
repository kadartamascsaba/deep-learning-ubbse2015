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
import com.voiceconf.voiceconf.networking.services.LoginCallback;
import com.voiceconf.voiceconf.networking.services.LoginService;
import com.voiceconf.voiceconf.storage.models.User;
import com.voiceconf.voiceconf.ui.main.MainActivity;


/**
 * This activity handles the user login.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final String GOOGLE_USER_ID = "{\"GoogleUserID\" : \"";
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
                if(acct.getPhotoUrl()==null){
                    somethingWentWrong("Google avatar required!");
                }
                // Request was successful => checking if the user exists
                LoginService.login(new LoginCallback() {
                    @Override
                    public void onSucces() {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onFailure(Exception e, String message) {
                        somethingWentWrong(message + " " + e.toString());
                    }
                }, acct.getDisplayName(), acct.getPhotoUrl().toString(), GOOGLE_USER_ID, acct.getId(), acct.getEmail());
            } else {
                somethingWentWrong(getResources().getString(R.string.something_went_wrong));
            }
        }
    }

    /**
     * Something went wrong notifying the user
     */
    private void somethingWentWrong(String msg) {
        Snackbar.make(mSignInButton, msg, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSignInButton.performClick();
                    }
                })
                .show();
    }
}
