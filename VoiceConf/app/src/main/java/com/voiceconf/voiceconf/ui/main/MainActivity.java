package com.voiceconf.voiceconf.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.voiceconf.voiceconf.storage.models.User;
import com.voiceconf.voiceconf.ui.conference.ConferenceDetailActivity;
import com.voiceconf.voiceconf.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This is the main activity witch greets the user contains a tabbed layout with recent user
 * activity and friend management.
 * <p/>
 * Created by Attila Blenesi 20/12/2015
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private static final String ADD_FRIEND_DIALOG_TAG = "add_friend";
    private FloatingActionButton mFloatingActionButton;
    private ViewPager mViewPager;

    //region LIFE CYCLE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Menu drawer layout setup
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation setup
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ParseUser currentUser = User.getCurrentUser();

        View header = navigationView.getHeaderView(0);
        CircleImageView avatarView = (CircleImageView) header.findViewById(R.id.user_avatar);
        TextView emailView = (TextView) header.findViewById(R.id.user_email);
        TextView nameView = (TextView) header.findViewById(R.id.user_name);

        Glide.with(this).load(User.getAvatar(currentUser)).into(avatarView);
        nameView.setText(currentUser.getUsername());
        emailView.setText(currentUser.getEmail());

        // Floating Action Button setup
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mViewPager.getCurrentItem()){
                    case MainPagerAdapter.HISTORY_TAB:
                        startActivity(new Intent(MainActivity.this, ConferenceDetailActivity.class));
                        break;
                    case MainPagerAdapter.FRIENDS_TAB:
                        new AddFriendDialog().show(getSupportFragmentManager(), ADD_FRIEND_DIALOG_TAG);
                }
            }
        });

        // Tab layout and ViewPager setup
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion

    //region VIEW PAGER ON CHANGE
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case MainPagerAdapter.FRIENDS_TAB:
                mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person_add_white_24dp));
                break;
            case MainPagerAdapter.HISTORY_TAB:
                mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_start_conference_white_24dp));
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //endregion
}
