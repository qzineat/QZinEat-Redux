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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.qe.qzin.QZinApplication;
import com.qe.qzin.R;
import com.qe.qzin.adapters.EventsAdapter;
import com.qe.qzin.listeners.EndlessRecyclerViewScrollListener;
import com.qe.qzin.listeners.OnEventClickListener;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;
import com.qe.qzin.util.QZinUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener, OnEventClickListener
{

  private List<Event> events;
  private EventsAdapter eventsAdapter;
  private EndlessRecyclerViewScrollListener scrollListener;
  private static final int DISPLAY_LIMIT = 5;

  @BindView(R.id.rvEvents) RecyclerView rvEvents;
  @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
  @BindView(R.id.progressBar) ProgressBar progressBar;
  @BindView(R.id.drawer_layout) DrawerLayout drawer;
  @BindView(R.id.nav_view) NavigationView navView;
  ImageView ivProfileEdit;
  TextView tvNavHeaderUserName;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    QZinUtil.onActivityCreateSetTheme(this); // Change Theme
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    // Drawer config
    setDrawerTheme();

    navView.setNavigationItemSelectedListener(this);

    View navHeaderView = navView.getHeaderView(0);

    if(User.getCurrentUser() != null){
      tvNavHeaderUserName = (TextView) navHeaderView.findViewById(R.id.textViewHeaderName);
      ivProfileEdit = (ImageView)  navHeaderView.findViewById(R.id.ivProfileEdit);

      setUserDetails();

      ivProfileEdit.setVisibility(View.VISIBLE);
      // Edit Profile
      ivProfileEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(MainActivity.this, UpdateProfileActivity.class);
          startActivity(intent);
        }
      });
    }


    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    // create events adapter
    // set adapter on recycleview
    events = new ArrayList<Event>();
    eventsAdapter = new EventsAdapter(this, events);
    rvEvents.setAdapter(eventsAdapter);
    rvEvents.setLayoutManager(linearLayoutManager);

    addSwipeRefresh();                          // Swipe Refresh
    addInfiniteScrolling(linearLayoutManager);  // infinite scrolling

    loadEventData(0);
  }

  private void setDrawerTheme() {
    if(User.isLoggedIn()){
      if(QZinApplication.isHostView){
        navView.setBackgroundColor(getResources().getColor(R.color.colorGray));
        navView.setItemIconTintList(getResources().getColorStateList(R.color.white));
        navView.setItemTextColor(getResources().getColorStateList(R.color.white));
      }
    }
  }

  // Swipe Refresh
  private void addSwipeRefresh(){
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
  }

  // load data in recycleview on infinite scrolling
  private void addInfiniteScrolling(LinearLayoutManager linearLayoutManager){
    // load data in recycleview on infinite scrolling
    scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        loadEventData(page);
      }
    };

    rvEvents.addOnScrollListener(scrollListener);
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
      // Set Application variables
      if(QZinApplication.isHostView){
        QZinApplication.isHostView = !QZinApplication.isHostView;
      }
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
    }else if(id == R.id.nav_registered_events){
      Intent intent = new Intent(MainActivity.this, RegisteredEventsActivity.class);
      startActivity(intent);
    }else if(id == R.id.nav_switch){
      QZinUtil.changeTheme(this);
      return false;
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }


  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    Menu menuNav = navView.getMenu();

    // switch to hosting text
    MenuItem switchAccount = menuNav.findItem(R.id.nav_switch);
    if(QZinApplication.isHostView){
      switchAccount.setTitle(R.string.switch_to_search);
    }else{
      switchAccount.setTitle(R.string.switch_to_hosting);
    }

    //hide show
    menuNav.findItem(R.id.nav_host_event).setVisible(QZinApplication.isHostView);   // On Login & HostView only
    menuNav.findItem(R.id.nav_hosted_events).setVisible(QZinApplication.isHostView);// On Login & HostView only
    menuNav.findItem(R.id.nav_registered_events).setVisible(User.isLoggedIn() && !QZinApplication.isHostView); // On Login only
    switchAccount.setVisible(User.isLoggedIn());                                    // On Login only
    menuNav.findItem(R.id.nav_logout).setVisible(User.isLoggedIn());                // On Login only
    menuNav.findItem(R.id.nav_login).setVisible(!User.isLoggedIn());                // Before login


    return true;
  }






  // Read data from Event parse object
  // Notify adapter about data change in events list
  // pagination and endless scrolling
  private void loadEventData(int offset) {

    progressBar.setVisibility(View.VISIBLE);

    ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
    query.setLimit(DISPLAY_LIMIT);
    query.setSkip(offset * DISPLAY_LIMIT);

    query.findInBackground(new FindCallback<Event>() {
      @Override
      public void done(List<Event> eventList, ParseException exception) {

        if(exception == null){
          eventsAdapter.addAll(eventList);
        }

        progressBar.setVisibility(View.INVISIBLE);
      }
    });
  }




  @Override
  public void onEventClickListener(int position) {
    Event event = eventsAdapter.getEventAtPosition(position);
    //Toast.makeText(this, "Title - " + event.getTitle(), Toast.LENGTH_SHORT).show();

    //ParseProxyObject proxyEvent = new ParseProxyObject(event);

    // Call Detail Event Activity
    Intent intent = new Intent(this, EventDetailActivity.class);
    //intent.putExtra("proxyEvent", proxyEvent);
    intent.putExtra("eventObjectId", event.getObjectId());
    startActivity(intent);
  }

  @Override
  protected void onResume() {
    super.onResume();

    setUserDetails();
  }

  private void setUserDetails(){
    if(User.getCurrentUser() != null){
      User u = (User) User.getCurrentUser();

      if(u.getFirstName() != null){
        tvNavHeaderUserName.setText(u.getFirstName());
      }else{
        tvNavHeaderUserName.setText(u.getEmail());
      }
    }
  }
}
