package com.qe.qzin.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qe.qzin.R;
import com.qe.qzin.activities.MainActivity;
import com.qe.qzin.listeners.OnEventClickListener;
import com.qe.qzin.models.Event;
import com.qe.qzin.util.DateTimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

  private List<Event> mEvents;
  private Context mContext;

  public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.ivEvent) ImageView ivEvent;
    @BindView(R.id.tvEventTitle) TextView tvEventTitle;
    @BindView(R.id.tvLocality) TextView tvLocality;
    @BindView(R.id.tvEventDate) TextView tvEventDate;
    @BindView(R.id.tvEventDescription) TextView tvEventDescription;
    @BindView(R.id.ivShare) ImageView ivShare;
    @BindView(R.id.tvPrice) TextView tvPrice;


    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      view.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
      Context context = view.getContext();
      int position = getAdapterPosition();
      if (context instanceof MainActivity) {
        ((OnEventClickListener) context).onEventClickListener(position);
      }
      //Toast.makeText(view.getContext(), "position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
    }
  }


  public EventsAdapter(Context context, List<Event> events) {
    mEvents = events;
    mContext = context;
  }


  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    LayoutInflater inflater = LayoutInflater.from(parent.getContext());

    View convetView = inflater.inflate(R.layout.item_event, parent, false);

    ViewHolder viewHolder = new ViewHolder(convetView);

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(final ViewHolder viewHolder, int position) {

    final Event event = mEvents.get(position);

    // populate the values from above event item
    if (event.getEventImageUrl() == null) {
      // TODO: Fix this later - Remove loading remote image and have drawable placeholder in Picasso
      Picasso.with(mContext)
          .load(R.drawable.ev_image)
          .fit()
          .into(viewHolder.ivEvent);
    } else {
      Picasso.with(mContext)
          .load(event.getEventImageUrl())
          .fit()
          .placeholder(R.drawable.ev_image)
          .error(R.drawable.ev_image)
          .into(viewHolder.ivEvent);
    }

    //set event title, description, locality.
    viewHolder.tvEventTitle.setText(event.getTitle());
    viewHolder.tvLocality.setText(event.getLocality());
    viewHolder.tvEventDescription.setText(event.getDescription());

    //get currency
    // TODO: update this using Java currency
    String currSymbol = "$";
    if (event.getCurrency() != null) {
      switch (event.getCurrency()) {
        case "USD":
          currSymbol = "$";
          break;

        default:
          currSymbol = "$";
          break;
      }
    }

    if ((Double) event.getAmount() != null) {
      int priceInt = (int) event.getAmount();
      if (priceInt == event.getAmount()) { // if whole amount. eg $40
        viewHolder.tvPrice.setText(currSymbol + String.format("%d", priceInt));
      } else { // if amount is not whole amount. eg $40.10
        viewHolder.tvPrice.setText(currSymbol + String.format("%.2f", event.getAmount()));
      }
    }

    if (event.getDate() != null) {
      viewHolder.tvEventDate.setText(DateTimeUtils.formatDate(event.getDate()));
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

  public Event getEventAtPosition(int position){
    return mEvents.get(position);
  }
}
