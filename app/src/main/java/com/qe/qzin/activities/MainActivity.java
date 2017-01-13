package com.qe.qzin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.qe.qzin.R;
import com.qe.qzin.adapters.EventsAdapter;
import com.qe.qzin.listeners.EndlessRecyclerViewScrollListener;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.qe.qzin.R.id.nav_view;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

  List<Event> events;
  EventsAdapter eventsAdapter;
  @BindView(R.id.rvEvents) RecyclerView rvEvents;
  private EndlessRecyclerViewScrollListener scrollListener;

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


    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    events = getEventData();
    eventsAdapter = new EventsAdapter(this,events);
    rvEvents.setAdapter(eventsAdapter);
    rvEvents.setLayoutManager(linearLayoutManager);

    scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        loadMoreEventData(page);
      }
    };

    rvEvents.addOnScrollListener(scrollListener);

    eventsAdapter.notifyItemRangeInserted(eventsAdapter.getItemCount(),events.size());

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


  private List<Event> getEventData() {

    final List<Event> events = new ArrayList<Event>();

    ParseQuery<Event> query = ParseQuery.getQuery("Event");
    query.findInBackground(new FindCallback<Event>() {
      @Override
      public void done(List<Event> eventList, ParseException exception) {

        if(exception == null){
          events.addAll(eventList);
        }

      }
    });

    return events;
  }

  private ArrayList<Event> loadMoreEventData(int page){
    // to be implemented
    //fetch next records from event table in database
    return null;
  }
}
