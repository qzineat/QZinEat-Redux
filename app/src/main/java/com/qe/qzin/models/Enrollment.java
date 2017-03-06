package com.qe.qzin.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Enrollment")
public class Enrollment extends ParseObject{

  private static final String KEY_USER_ID = "userId";
  private static final String KEY_EVENT_ID = "eventId";
  private static final String KEY_GUEST_COUNT = "guestCount";


  //default constructor
  public Enrollment(){
  }

  public ParseUser getKeyUserId() {
    return getParseUser(KEY_USER_ID);
  }

  public String getKeyEventId() {
    return getString(KEY_EVENT_ID);
  }

  public int getKeyGuestCount() {
    return getInt(KEY_GUEST_COUNT);
  }

  public void setUserId(ParseUser userId) {
     put(KEY_USER_ID, userId);
  }

  public void setEventId(String eventId) {
    put(KEY_EVENT_ID, eventId);
  }

  public void setGuestCount(int guestCount) {
    put(KEY_GUEST_COUNT, guestCount);
  }

}
