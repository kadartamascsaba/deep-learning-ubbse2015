package com.voiceconf.voiceconf.storage.nonpersistent;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Attila Blenesi on 01 Jan 2016
 */
public class DataManagerTest extends TestCase {

    public void testGetUsers() throws Exception {
        // For further testing ParseUser had to be mocked
        DataManager testDataManager = new DataManager();
        assertEquals(testDataManager.getUsers(), null);
    }

    public void testGetUsers1() throws Exception {
        // For further testing ParseUser had to be mocked
        DataManager testDataManager = new DataManager();
        assertEquals(testDataManager.getUsers(new ArrayList<String>()), null);
    }
}