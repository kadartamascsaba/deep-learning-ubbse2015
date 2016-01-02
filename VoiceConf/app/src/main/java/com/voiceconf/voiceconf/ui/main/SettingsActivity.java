package com.voiceconf.voiceconf.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.storage.nonpersistent.SharedPreferenceManager;
import com.voiceconf.voiceconf.util.Validator;

/**
 * Created by Tamas-Csaba Kadar on 01 Jan 2016
 * Edited by Attila Blenesi
 */
public class SettingsActivity extends AppCompatActivity {

    private EditText mHostname;
    private EditText mPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHostname = (EditText)findViewById(R.id.set_hostname_input);
        mPort = (EditText)findViewById(R.id.set_port_input);

        mHostname.setText(SharedPreferenceManager.getInstance(this).getSavedIpAddress());
        String port = SharedPreferenceManager.getInstance(this).getSavedPort();
        if (!port.equals("")) {
            mPort.setText(port);
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.settings));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_save:
                String hostname = mHostname.getText().toString();
                String port = mPort.getText().toString();
                if (Validator.isValidIpAddress(hostname) && Validator.isValidPort(port)) {
                    SharedPreferenceManager.getInstance(this).saveServerData(hostname, port);
                }
                else {
                    Snackbar.make(mPort, "Given server data is invalid", Snackbar.LENGTH_LONG).show();
                }
                return true;
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
