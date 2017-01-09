package com.qe.qzin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.parse.ParseUser;
import com.qe.qzin.R;
import com.qe.qzin.models.User;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

import static com.qe.qzin.R.id.nav_view;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(nav_view);
    navigationView.setNavigationItemSelectedListener(this);


    // Event
    ImageView ivEvent = (ImageView) findViewById(R.id.ivEvent);
    Picasso.with(getApplicationContext())
        .load("http://blog.logomyway.com/wp-content/uploads/2013/06/143.jpg")
        .fit()
        .into(ivEvent);


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

    if (id == R.id.nav_login) {
      Intent intentLoginSignup = new Intent(MainActivity.this, AuthActivity.class);
      startActivity(intentLoginSignup);
      //finish();
    } else if (id == R.id.nav_logout){
      ParseUser.logOut();
      Intent refresh = new Intent(this, MainActivity.class);
      startActivity(refresh);
      finish();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }


  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {

   setLoginLogoutOptionInNavMenu();
    return true;
  }


  // if user already registered and logged in show Logout option in navigation menu
  // if user not logged in show Login option in navigation menu
  public void setLoginLogoutOptionInNavMenu()
  {
    NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
    Menu menuNav = navView.getMenu();
    MenuItem login = menuNav.findItem(R.id.nav_login);
    MenuItem logout = menuNav.findItem(R.id.nav_logout);

    login.setVisible(!User.isLoggedIn());
    logout.setVisible(User.isLoggedIn());
  }
}
