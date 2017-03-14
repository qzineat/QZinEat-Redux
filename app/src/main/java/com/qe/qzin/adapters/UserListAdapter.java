package com.qe.qzin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qe.qzin.R;
import com.qe.qzin.models.User;

import java.util.List;

/**
 * Created by Shyam Rokde on 3/13/17.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserViewHolder> {

  private Context mContext;
  private List<User> mUsers;

  public UserListAdapter(Context context, List<User> users) {
    this.mContext = context;
    this.mUsers = users;
  }

  @Override
  public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View convetView = inflater.inflate(R.layout.item_user, parent, false);

    return new UserViewHolder(convetView);
  }

  @Override
  public void onBindViewHolder(UserViewHolder viewHolder, int position) {
    User user = mUsers.get(position);

    viewHolder.tvName.setText(user.getFirstName());
  }

  @Override
  public int getItemCount() {
    return mUsers.size();
  }

  /**
   * Remove All data from adapter
   */
  public void clear() {
    mUsers.clear();
    notifyDataSetChanged();
  }

  /**
   * Add multiple items into adapter
   * @param list
   */
  public void addAll(List<User> list){
    mUsers.addAll(list);
    notifyItemRangeInserted(getItemCount(), list.size());
  }
}
