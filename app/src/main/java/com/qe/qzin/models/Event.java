package com.qe.qzin.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Shyam Rokde on 1/11/17.
 */
@ParseClassName("Event")
public class Event extends ParseObject {

  public static final String KEY_HOST_USER = "hostUser";
  public static final String KEY_EVENT_DATE = "eventDate";

  private static final String KEY_TITLE = "title";
  private static final String KEY_DESCRIPTION = "description";
  private static final String KEY_AMOUNT = "amount";
  private static final String KEY_CURRENCY = "currency";
  private static final String KEY_EVENT_TIME_FROM = "eventTimeFrom";
  private static final String KEY_EVENT_TIME_TO = "eventTimeTo";
  private static final String KEY_EVENT_IMAGE_URL = "eventImageUrl";
  private static final String KEY_MAX_GUEST_COUNT = "maxGuestCount";
  private static final String KEY_ENROLLED_GUEST_COUNT = "enrolledGuestCount";

  // Address
  private static final String KEY_STREET_ADDRESS = "street_address";
  private static final String KEY_LOCALITY = "locality";                      // also known as city
  private static final String KEY_ADMINISTRATIVE_AREA = "administrativeArea"; // also known as state
  private static final String KEY_COUNTRY = "country";

  public Event() {
    // Required for Parse
  }

  public User getHostUser() {
    return (User) getParseUser(KEY_HOST_USER);
  }

  public void setHostUser(ParseUser hostUser) {
    put(KEY_HOST_USER, hostUser);
  }

  public String getTitle() {
    return getString(KEY_TITLE);
  }

  public void setTitle(String title) {
    put(KEY_TITLE, title);
  }

  public String getDescription() {
    return getString(KEY_DESCRIPTION);
  }

  public void setDescription(String description) {
    put(KEY_DESCRIPTION, description);
  }

  public double getAmount(){
    return getDouble(KEY_AMOUNT);
  }

  public void setAmount(double amount){
    put(KEY_AMOUNT, amount);
  }

  public String getCurrency(){
    return getString(KEY_CURRENCY);
  }

  public void setCurrency(String currency){
    put(KEY_CURRENCY, currency);
  }

  public Date getDate() {
    return getDate(KEY_EVENT_DATE);
  }

  public void setDate(Date date) {
    put(KEY_EVENT_DATE, date);
  }

  public String getStreetAddress() {
    return getString(KEY_STREET_ADDRESS);
  }

  public void setStreetAddress(String streetAddress) {
    put(KEY_STREET_ADDRESS, streetAddress);
  }

  public String getLocality() {
    return getString(KEY_LOCALITY);
  }

  public void setLocality(String locality) {
    put(KEY_LOCALITY, locality);
  }

  public String getAdministrativeArea() {
    return getString(KEY_ADMINISTRATIVE_AREA);
  }

  public void setAdministrativeArea(String administrativeArea) {
    put(KEY_ADMINISTRATIVE_AREA, administrativeArea);
  }

  public String getCountry() {
    return getString(KEY_COUNTRY);
  }

  public void setCountry(String country) {
    put(KEY_COUNTRY, country);
  }

  public String getEventImageUrl() {
    return getString(KEY_EVENT_IMAGE_URL);
  }

  public void setEventImageUrl(String eventImageUrl) {
    put(KEY_EVENT_IMAGE_URL, eventImageUrl);
  }

  public String getEventTimeFrom() {
    return getString(KEY_EVENT_TIME_FROM);
  }

  public void setEventTimeFrom(String eventTimeFrom) {
    put(KEY_EVENT_TIME_FROM, eventTimeFrom);
  }

  public String getEventTimeTo() {
    return getString(KEY_EVENT_TIME_TO);
  }

  public void setEventTimeTo(String eventTimeTo) {
    put(KEY_EVENT_TIME_TO, eventTimeTo);
  }

  public int getMaxGuestCount() {
    return getInt(KEY_MAX_GUEST_COUNT);
  }

  public void setMaxGuestCount(int maxGuestCount) {
    put(KEY_MAX_GUEST_COUNT, maxGuestCount);
  }

  public int getEnrolledGuestCount() {
    return getInt(KEY_ENROLLED_GUEST_COUNT);
  }

  public void setEnrolledGuestCount(int enrolledGuestCount) {
    put(KEY_ENROLLED_GUEST_COUNT, enrolledGuestCount);
  }
}
