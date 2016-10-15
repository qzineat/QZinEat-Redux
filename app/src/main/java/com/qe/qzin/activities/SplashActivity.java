package com.qe.qzin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qe.qzin.R;

public class SplashActivity extends AppCompatActivity {

  private final int SPLASH_DISPLAY_LENGTH = 2500;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    // TODO: We should call REST API  and load data before loading main activity
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
      }
    }, SPLASH_DISPLAY_LENGTH);
  }
}
