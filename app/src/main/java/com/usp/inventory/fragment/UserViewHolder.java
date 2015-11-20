package com.usp.inventory.fragment;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.usp.inventory.R;
import com.usp.inventory.activity.InventoryActivity;
import com.usp.inventory.model.User;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by umasankar on 11/16/15.
 */
public class UserViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.thumbnail)
    public ImageView thumbnail;

    @Bind(R.id.user_display_name)
    public TextView nameTextView;

    @Bind(R.id.user_email)
    public TextView emailTextView;

    private User user;
    private View view;

    public UserViewHolder(View userView) {
        super(userView);
        ButterKnife.bind(this, userView);
        userView.setOnClickListener(onClickListener);
        this.view = userView;
    }

    public void setUser(User user, String uid) {
        if (user.getUid().equals(uid)) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        this.user = user;
        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.getContext().startActivity(InventoryActivity.getIntent(v.getContext(), user));
        }
    };
}
