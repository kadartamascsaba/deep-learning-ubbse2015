package com.voiceconf.voiceconf.storage.nonpersistent;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Attila Blenesi on 03 Jan 2016
 */
public class DataManagerTest {

    DataManager mDataManager;

    @Before
    public void setUp() throws Exception {
        mDataManager = new DataManager();
    }

    @Test
    public void testGetFriends() throws Exception {
        assertEquals(1,0);
    }

    @Test
    public void testSetConferences() throws Exception {
        assertEquals(1,1);

    }

    @Test
    public void testGetConferences() throws Exception {
        assertEquals(1,1);

    }

    @Test
    public void testGetUsers() throws Exception {
        assertEquals(1,1);

    }

    @Test
    public void testGetUsers1() throws Exception {
        assertEquals(1,1);

    }

    @Test
    public void testGetConference() throws Exception {
        assertEquals(1,1);

    }
}