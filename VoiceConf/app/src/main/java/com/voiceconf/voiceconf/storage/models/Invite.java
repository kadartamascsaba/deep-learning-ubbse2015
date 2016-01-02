package com.voiceconf.voiceconf.storage.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Attila Blenesi on 01 Jan 2016
 */
@ParseClassName("Invite")
public class Invite extends ParseObject{

    //region CONSTANTS
    public static final String CONFERENCE = "conference";
    public static final String ACCEPTED = "accepted";
    public static final String INVITED = "invited";
    //endregion

    //region GETTERS/SETTERS
    public ParseUser getInvited() { return getParseUser(INVITED); }

    public void setInvited(String invitedId){
        put(INVITED, ParseUser.createWithoutData(ParseUser.class, invitedId));
    }

    public ParseObject getConference() { return getParseObject(CONFERENCE); }

    public  void setConference(String conferenceId){
        put(CONFERENCE, ParseObject.createWithoutData(Conference.class, conferenceId));
    }

    public boolean isAccepted(){
        return getBoolean(ACCEPTED);
    }

    public void setAccepted(boolean accepted){
        put(ACCEPTED, accepted);
    }

    public String getConferenceId() {
        return getParseObject(CONFERENCE).getObjectId();
    }
    //endregion
}
