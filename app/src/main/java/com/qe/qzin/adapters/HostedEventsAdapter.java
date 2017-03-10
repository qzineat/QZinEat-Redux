package com.qe.qzin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qe.qzin.R;
import com.qe.qzin.listeners.OnEventRemoveListener;
import com.qe.qzin.models.Event;
import com.qe.qzin.util.DateTimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Shyam Rokde on 3/3/17.
 */

public class HostedEventsAdapter extends RecyclerView.Adapter<HostedEventsViewHolder> {
  private List<Event> mEvents;
  private Context mContext;
  private OnEventRemoveListener eventRemoveListener;

  public HostedEventsAdapter(Context context, List<Event> events, OnEventRemoveListener listener) {
    mEvents = events;
    mContext = context;
    eventRemoveListener = listener;
  }

  @Override
  public HostedEventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View convetView = inflater.inflate(R.layout.item_hosted_event, parent, false);

    return new HostedEventsViewHolder(convetView);
  }

  @Override
  public void onBindViewHolder(HostedEventsViewHolder viewHolder, int position) {
    Event event = mEvents.get(position);
    // populate the values from above event item
    if(event.getEventImageUrl() == null){
      // TODO: Fix this later - Remove loading remote image and have drawable placeholder in Picasso
      Picasso.with(mContext)
          .load("http://blog.logomyway.com/wp-content/uploads/2013/06/143.jpg")
          .fit()
          .into(viewHolder.ivEventImage);
    }else{
      Picasso.with(mContext)
          .load(event.getEventImageUrl())
          .fit()
          .into(viewHolder.ivEventImage);
    }
    viewHolder.tvEventTitle.setText(event.getTitle());
    viewHolder.tvLocality.setText(event.getLocality());
    if(event.getDate() !=null) {
      DateFormat df = new DateFormat();
      viewHolder.tvEventDate.setText(DateTimeUtils.formatDate(event.getDate()));
    }
    // Guest Count Data
    int maxGuestCount = event.getMaxGuestCount();
    int enrolledGuestCount = event.getEnrolledGuestCount();
    viewHolder.tvGuestCountData.setText(String.format("%d/%d", enrolledGuestCount, maxGuestCount));
  }

  @Override
  public int getItemCount() {
    return mEvents.size();
  }

  /**
   * Remove All data from adapter
   */
  public void clear() {
    mEvents.clear();
    notifyDataSetChanged();
  }

  /**
   * Remove one item from adapter
   * @param position
   */
  public void remove(int position){

    Event event = mEvents.get(position);

    if(eventRemoveListener != null){
      eventRemoveListener.onEventRemoveListener(event);
    }

    // remove from adapter
    mEvents.remove(position);
    notifyItemRemoved(position);
  }

  /**
   * Add multiple items into adapter
   * @param list
   */
  public void addAll(List<Event> list){
    mEvents.addAll(list);
    notifyItemRangeInserted(getItemCount(), list.size());
  }
}
