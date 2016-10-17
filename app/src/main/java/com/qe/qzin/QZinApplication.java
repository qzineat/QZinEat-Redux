package com.qe.qzin;

import android.app.Application;

import com.parse.Parse;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by Shyam Rokde on 10/14/16.
 */

public class QZinApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Parse Setup
    setupParse();
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


    // Parse initialize
    Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("qzineat")
        .clientKey("qzineat-master-key-2016")
        .addNetworkInterceptor(new ParseLogInterceptor())
        .server("https://qzin.herokuapp.com/parse/")
        .build());
  }
}
