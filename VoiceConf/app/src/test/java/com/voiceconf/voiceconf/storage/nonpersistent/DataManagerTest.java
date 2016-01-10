package com.voiceconf.voiceconf.storage.nonpersistent;

import com.parse.ParseObject;
import com.voiceconf.voiceconf.storage.models.Conference;
import com.voiceconf.voiceconf.storage.models.Friend;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Attila Blenesi on 03 Jan 2016
 */
public class DataManagerTest {

    DataManager mDataManager;

    @Before
    public void setUp() throws Exception {
        mDataManager = new DataManager();
        ParseObject.registerSubclass(Friend.class);
    }

    @Test
    public void testGetFriends() throws Exception {
        assertNull(mDataManager.getFriends());
        ArrayList<Friend> friends = new ArrayList<>();
        mDataManager.setFriends(friends);
        assertNotEquals(mDataManager.getFriends(), null);
        friends.add(new Friend());
        friends.add(new Friend());
        friends.add(new Friend());
        assertEquals(mDataManager.getFriends().size(), 3);
    }

    @Test
    public void testGetConferences() throws Exception {
        assertNull(mDataManager.getConferences());
        ArrayList<Conference> conferences = new ArrayList<>();
        mDataManager.setConferences(conferences);
        assertNotEquals(mDataManager.getConferences(), null);
    }

    @Test
    public void testGetUsers() throws Exception {
        assertNull(mDataManager.getUsers(new ArrayList<String>()));
    }

    @Test
    public void testGetConference() throws Exception {
        ArrayList<Conference> conferences = new ArrayList<>();
        mDataManager.setConferences(conferences);
        assertNull(mDataManager.getConference("alma"));
    }
}