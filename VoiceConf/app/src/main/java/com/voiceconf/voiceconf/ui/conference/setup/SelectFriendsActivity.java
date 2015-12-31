package com.voiceconf.voiceconf.ui.conference.setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.parse.ParseUser;
import com.voiceconf.voiceconf.R;
import com.voiceconf.voiceconf.ui.view.PlaceholderRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectFriendsActivity extends AppCompatActivity {

    //region VARIABLES
    private static final int RESULT_CODE = 1; // Activity result code
    private static final String SELECTED_USER_IDS = "user_ids"; // Activity result message

    private List<ParseUser> mUsers;
    private ArrayMap<Integer, Boolean> mSelected;

    private SelectFriendRecyclerAdapter mRecyclerAdapter;
    private PlaceholderRecyclerView mRecyclerView;
    //endregion

    //region LIFE CYCLE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friends);

        // Toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialising helper variables
        mUsers = new ArrayList<>();
        mSelected = new ArrayMap<>();

        mRecyclerAdapter = new SelectFriendRecyclerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = mUsers.indexOf(v.getTag());
                //noinspection SimplifiableConditionalExpression [This satatement is more logical]
                mSelected.put(index, mSelected.containsKey(index) ? !mSelected.get(index) : true);
            }
        });

        mRecyclerView = (PlaceholderRecyclerView) findViewById(R.id.select_friends_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setEmptyView(findViewById(R.id.no_friend_placeholder));

        // Floating Action Button setup
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                ArrayList<String> friendIds = new ArrayList<>();
                for (Map.Entry<Integer, Boolean> entry : mSelected.entrySet()){
                    if(entry.getValue()){
                        friendIds.add(mUsers.get(entry.getKey()).getObjectId());
                    }
                }
                result.putStringArrayListExtra(SELECTED_USER_IDS, friendIds);
                setResult(RESULT_CODE, result);
            }
        });
    }
    //endregion
}
