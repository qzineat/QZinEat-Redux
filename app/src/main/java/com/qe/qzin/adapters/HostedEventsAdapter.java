package com.qe.qzin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qe.qzin.R;
import com.qe.qzin.models.Event;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Shyam Rokde on 3/3/17.
 */

public class HostedEventsAdapter extends RecyclerView.Adapter<HostedEventsViewHolder> {
  private List<Event> mEvents;
  private Context mContext;

  public HostedEventsAdapter(Context context, List<Event> events) {
    mEvents = events;
    mContext = context;
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
      viewHolder.tvEventDate.setText(df.format("MMM dd, yyyy", event.getDate()));
    }
  }

  @Override
  public int getItemCount() {
    return mEvents.size();
  }

  public void clear() {
    mEvents.clear();
    notifyDataSetChanged();
  }

  public void addAll(List<Event> list){
    mEvents.addAll(list);
    notifyItemRangeInserted(getItemCount(), list.size());
  }
}
