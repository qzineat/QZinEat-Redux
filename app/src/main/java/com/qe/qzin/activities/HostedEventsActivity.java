package com.qe.qzin.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.qe.qzin.R;
import com.qe.qzin.adapters.HostedEventsAdapter;
import com.qe.qzin.listeners.EndlessRecyclerViewScrollListener;
import com.qe.qzin.listeners.OnEventClickListener;
import com.qe.qzin.listeners.OnEventRemoveListener;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HostedEventsActivity extends BaseActivity implements OnEventRemoveListener, OnEventClickListener {

  @BindView(R.id.rvEvents) RecyclerView rvEvents;
  @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.progressBar) ProgressBar progressBar;

  private List<Event> mEvents;
  private HostedEventsAdapter hostedEventsAdapter;
  private EndlessRecyclerViewScrollListener scrollListener;
  private int displayLimit = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hosted_events);

    ButterKnife.bind(this);

    toolbar.setTitle("Hosted Events");

    setSupportActionBar(toolbar);
    // add back arrow to toolbar
    if (getSupportActionBar() != null){
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    mEvents = new ArrayList<>();

    hostedEventsAdapter = new HostedEventsAdapter(getApplicationContext(), mEvents, this);

    rvEvents.setAdapter(hostedEventsAdapter);
    rvEvents.setLayoutManager(linearLayoutManager);

    addSwipeRefresh();
    addInfiniteScrolling(linearLayoutManager);

    // add swipe left feature
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(rvEvents);
    setUpAnimationDecoratorHelper();

    // load first page
    loadHostedEvents(0);
  }

  // Swipe Refresh
  private void addSwipeRefresh(){
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        hostedEventsAdapter.clear();
        loadHostedEvents(0);
        swipeContainer.setRefreshing(false);
      }
    });

    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(
        android.R.color.holo_blue_bright,
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
        loadHostedEvents(page);
      }
    };

    rvEvents.addOnScrollListener(scrollListener);
  }


  /**
   * Load Hosted Events
   *
   * @param offset
   */
  private void loadHostedEvents(int offset) {

    progressBar.setVisibility(View.VISIBLE);

    ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
    query.whereEqualTo(Event.KEY_HOST_USER, User.getCurrentUser());
    query.setLimit(displayLimit);
    query.setSkip(offset * displayLimit);
    query.orderByAscending(Event.KEY_EVENT_DATE);

    query.findInBackground(new FindCallback<Event>() {
      @Override
      public void done(List<Event> eventList, ParseException e) {
        if (e == null) {
          hostedEventsAdapter.addAll(eventList);
        } else {
          Log.e("ERROR", "Unable to load events from Parse: " + e.getMessage());
        }
        progressBar.setVisibility(View.INVISIBLE);
      }
    });

  }


  ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    // we want to cache these and not allocate anything repeatedly in the onChildDraw method
    Drawable background;
    Drawable xMark;
    int xMarkMargin;
    boolean initiated;

    private void init() {
      background = new ColorDrawable(Color.RED);
      xMark = ContextCompat.getDrawable(HostedEventsActivity.this, R.drawable.delete);
      xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
      xMarkMargin = (int) HostedEventsActivity.this.getResources().getDimension(R.dimen.activity_horizontal_margin);
      initiated = true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
      int swipedPosition = viewHolder.getAdapterPosition();
      HostedEventsAdapter adapter = (HostedEventsAdapter) rvEvents.getAdapter();
      adapter.remove(swipedPosition);

      //Toast.makeText(HostedEventsActivity.this, "Position: " + swipedPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
      View itemView = viewHolder.itemView;

      // not sure why, but this method get's called for viewholder that are already swiped away
      if (viewHolder.getAdapterPosition() == -1) {
        // not interested in those
        return;
      }

      if (!initiated) {
        init();
      }

      // draw red background
      background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
      background.draw(c);

      // draw x mark
      int itemHeight = itemView.getBottom() - itemView.getTop();
      int intrinsicWidth = xMark.getIntrinsicWidth();
      int intrinsicHeight = xMark.getIntrinsicWidth();

      int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
      int xMarkRight = itemView.getRight() - xMarkMargin;
      int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
      int xMarkBottom = xMarkTop + intrinsicHeight;
      xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
      xMark.draw(c);

      super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
  };

  private void setUpAnimationDecoratorHelper() {
    rvEvents.addItemDecoration(new RecyclerView.ItemDecoration() {

      // we want to cache this and not allocate anything repeatedly in the onDraw method
      Drawable background;
      boolean initiated;

      private void init() {
        background = new ColorDrawable(Color.RED);
        initiated = true;
      }

      @Override
      public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!initiated) {
          init();
        }
        // only if animation is in progress
        if (parent.getItemAnimator().isRunning()) {
          View lastViewComingDown = null;
          View firstViewComingUp = null;

          // this is fixed
          int left = 0;
          int right = parent.getWidth();

          // this we need to find out
          int top = 0;
          int bottom = 0;

          // find relevant translating views
          int childCount = parent.getLayoutManager().getChildCount();
          for (int i = 0; i < childCount; i++) {
            View child = parent.getLayoutManager().getChildAt(i);
            if (child.getTranslationY() < 0) {
              // view is coming down
              lastViewComingDown = child;
            } else if (child.getTranslationY() > 0) {
              // view is coming up
              if (firstViewComingUp == null) {
                firstViewComingUp = child;
              }
            }
          }

          if (lastViewComingDown != null && firstViewComingUp != null) {
            // views are coming down AND going up to fill the void
            top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
            bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
          } else if (lastViewComingDown != null) {
            // views are going down to fill the void
            top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
            bottom = lastViewComingDown.getBottom();
          } else if (firstViewComingUp != null) {
            // views are coming up to fill the void
            top = firstViewComingUp.getTop();
            bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
          }

          background.setBounds(left, top, right, bottom);
          background.draw(c);
        }
        super.onDraw(c, parent, state);
      }


    });
  }

  @Override
  public void onEventRemoveListener(Event event) {
    // Remove Event from backend
    event.deleteInBackground(new DeleteCallback() {
      @Override
      public void done(ParseException e) {
        if(e != null){
          Toast.makeText(HostedEventsActivity.this, "Unable to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }else {
          Toast.makeText(HostedEventsActivity.this, "Evented deleted successfully!!!", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  @Override
  public void onEventClickListener(int position) {
    Event event = hostedEventsAdapter.getEventAtPosition(position);

    Intent intent = new Intent(this, EventUsersActivity.class);
    intent.putExtra("eventId", event.getObjectId());
    intent.putExtra("eventTitle", event.getTitle());
    startActivity(intent);
  }
}