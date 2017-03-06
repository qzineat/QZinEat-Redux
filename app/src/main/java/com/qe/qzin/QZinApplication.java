package com.qe.qzin;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;

import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Shyam Rokde on 10/14/16.
 */

public class QZinApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Fonts
    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
        .setFontAttrId(R.attr.fontPath)
        .build());


    // Parse Setup
    setupParse();

    FacebookSdk.sdkInitialize(getApplicationContext());
    AppEventsLogger.activateApp(this);


    // TODO: Remove Test Event Add on DB
    //createEventForTest();
  }



  /**
   * Parse.initialize()
   *  set applicationId, and server server based on the values in the Heroku settings.
   *  clientKey is not needed unless explicitly configured
   *  any network interceptors must be added with the Configuration Builder given this syntax
   *  Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("myappid")                                 // should correspond to APP_ID env variable
        .clientKey("simplechat9876")                              // should correspond to MASTER_KEY env variable
        .addNetworkInterceptor(new ParseLogInterceptor())
        .server("https://simplechat9876.herokuapp.com/parse/")    // should correspond to SERVER_URL env variable
        .build());
   */
  private void setupParse(){
    // Register your parse models here
    ParseObject.registerSubclass(User.class);
    ParseObject.registerSubclass(Event.class);

    // Parse initialize
    Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("qzineat")
        .clientKey("qzineat-master-key-2016")
        //.addNetworkInterceptor(new ParseLogInterceptor())
        .server("https://qzin.herokuapp.com/parse/")
        .build());

    // ParseFacebookUtils should initialize the Facebook SDK
    ParseFacebookUtils.initialize(this);

  }


  // TODO: Remove this later
  private void createEventForTest() {
    
    Event event = new Event();
    event.setTitle("Modern Italian Feast");
    event.setLocality("San Francisco");
    event.setAdministrativeArea("CA");
    event.setDate(new Date());
    event.setDescription("Your culinary journey will include an elegant five-course tasting menu inspired by " +
        "rustic Northern Italian inspired cuisine married with a creative, modern twist.");
    event.setAmount(45);

    if(User.isLoggedIn()){
      event.setHostUser(User.getCurrentUser());
    }

    event.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e == null)
          Log.d("DEBUG","New Record Added in Events");
        else
          e.printStackTrace();
      }

    });
  }
}
