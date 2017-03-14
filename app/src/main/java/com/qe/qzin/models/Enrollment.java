package com.qe.qzin.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Enrollment")
public class Enrollment extends ParseObject{

  public static final String KEY_USER = "enrollUser";
  public static final String KEY_EVENT = "event";
  private static final String KEY_GUEST_COUNT = "guestCount";

  private User enrollUser;
  private Event event;
  private int guestCount;

  //default constructor
  public Enrollment(){
  }

  public User getEnrollUser() {
    enrollUser = (User) getParseUser(KEY_USER);
    return enrollUser;
  }

  public void setEnrollUser(User enrollUser) {
    this.enrollUser = enrollUser;
    put(KEY_USER, enrollUser);
  }

  public Event getEvent() {
    event = (Event) getParseObject(KEY_EVENT);
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
    put(KEY_EVENT, event);
  }

  public int getGuestCount() {
    guestCount = getInt(KEY_GUEST_COUNT);
    return guestCount;
  }

  public void setGuestCount(int guestCount) {
    this.guestCount = guestCount;
    put(KEY_GUEST_COUNT, guestCount);
  }
}
