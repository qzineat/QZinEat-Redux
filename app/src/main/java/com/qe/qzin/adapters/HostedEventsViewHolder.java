package com.qe.qzin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qe.qzin.R;
import com.qe.qzin.activities.HostedEventsActivity;
import com.qe.qzin.listeners.OnEventClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shyam Rokde on 3/3/17.
 */

public class HostedEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

  @BindView(R.id.ivEventImage) ImageView ivEventImage;
  @BindView(R.id.tvEventTitle) TextView tvEventTitle;
  @BindView(R.id.tvLocality) TextView tvLocality;
  @BindView(R.id.tvEventDate) TextView tvEventDate;
  @BindView(R.id.tvGuestCountData) TextView tvGuestCountData;

  public HostedEventsViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);

    itemView.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    Context context = view.getContext();
    int position = getAdapterPosition();
    if (context instanceof HostedEventsActivity) {
      ((OnEventClickListener) context).onEventClickListener(position);
    }
  }
}
