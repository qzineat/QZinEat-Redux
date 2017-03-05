package com.qe.qzin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

  private  List<Event> events;
  private EventsAdapter eventsAdapter;
  private EndlessRecyclerViewScrollListener scrollListener;
  TextView tvNavHeaderUserName;
  @BindView(R.id.rvEvents) RecyclerView rvEvents;
  @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;



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

    View navHeaderView = navigationView.getHeaderView(0);

    if(User.getCurrentUser() != null){

      tvNavHeaderUserName = (TextView) navHeaderView.findViewById(R.id.textViewHeaderName);
      tvNavHeaderUserName.setText(User.getCurrentUser().getUsername());

    }

    // Swipe Refresh
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {

        eventsAdapter.clear();
        loadEventData(0);
        swipeContainer.setRefreshing(false);
      }
    });

    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    // create events adapter
    // set adapter on recycleview
    events = new ArrayList<Event>();
    eventsAdapter = new EventsAdapter(events);
    rvEvents.setAdapter(eventsAdapter);
    rvEvents.setLayoutManager(linearLayoutManager);

    // load data in recycleview on infinite scrolling
    scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        loadEventData(page);
      }
    };

    rvEvents.addOnScrollListener(scrollListener);
    loadEventData(0);
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
      // on login click call Auth acivity
      Intent intentLoginSignup = new Intent(MainActivity.this, AuthActivity.class);
      startActivity(intentLoginSignup);
      //finish();
    } else if (id == R.id.nav_logout){
      // current user log out and navigate to events Stream.
      ParseUser.logOut();
      Intent refresh = new Intent(this, MainActivity.class);
      startActivity(refresh);
      finish();
    } else if(id == R.id.nav_host_event){
      Intent intent = new Intent(MainActivity.this, HostActivity.class);
      startActivity(intent);
    }else if(id == R.id.nav_hosted_events){
      Intent intent = new Intent(MainActivity.this, HostedEventsActivity.class);
      startActivity(intent);
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


  // Read data from Event parse object
  // Notify adapter about data change in events list
  // pagination and endless scrolling
  private void loadEventData(int offset) {

    int displayLimit = 5;
    ParseQuery<Event> query = ParseQuery.getQuery("Event");
    query.setLimit(displayLimit);
    query.setSkip(offset * displayLimit);

    query.findInBackground(new FindCallback<Event>() {
      @Override
      public void done(List<Event> eventList, ParseException exception) {

        if(exception == null){
          eventsAdapter.addAll(eventList);
        }
      }
    });
  }

}
