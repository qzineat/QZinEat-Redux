package com.qe.qzin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;
import com.qe.qzin.R;

import static com.qe.qzin.R.id.nav_view;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

  private boolean isUserRegistered = false;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    //get the current user from Parse
     ParseUser currentUser = ParseUser.getCurrentUser();
     // Log.d("DEBUG","Current User: "+ currentUser.getUsername());
      if(currentUser != null) {
        // send logged in user to home page
        isUserRegistered = true;
        Toast.makeText(getApplicationContext(),"Current User session", Toast.LENGTH_SHORT).show();
      }else{
        //send user to AuthActivity
        isUserRegistered = false;
        Intent intentLoginSignup = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intentLoginSignup);
        finish();
      }

  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      // Handle the camera action
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    } else if (id == R.id.nav_login) {

      Intent intentLoginSignup = new Intent(MainActivity.this, AuthActivity.class);
      startActivity(intentLoginSignup);
      finish();
    } else if (id == R.id.nav_logout){

      ParseUser.logOut();
      isUserRegistered = false;
      setLoginLogoutOptionInNavMenu(isUserRegistered);

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }


  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {

   setLoginLogoutOptionInNavMenu(isUserRegistered);
    return true;
  }


  // if user already registered and logged in show Logout option in navigation menu
  // if user not logged in show Login option in navigation menu
  public void setLoginLogoutOptionInNavMenu(boolean isUserRegistered)
  {
    NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
    Menu menuNav = navView.getMenu();
    MenuItem login = menuNav.findItem(R.id.nav_login);
    MenuItem logout = menuNav.findItem(R.id.nav_logout);

    login.setVisible(!isUserRegistered);
    logout.setVisible(isUserRegistered);
  }

}
