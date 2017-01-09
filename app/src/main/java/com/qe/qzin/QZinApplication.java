package com.qe.qzin;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;
import com.qe.qzin.models.User;

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
    //--ParseObject.registerSubclass(Message.class);
    ParseObject.registerSubclass(User.class);

    // Parse initialize
    Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("qzineat")
        .clientKey("qzineat-master-key-2016")
        .addNetworkInterceptor(new ParseLogInterceptor())
        .server("https://qzin.herokuapp.com/parse/")
        .build());

    // ParseFacebookUtils should initialize the Facebook SDK
    ParseFacebookUtils.initialize(this);

  }
}
