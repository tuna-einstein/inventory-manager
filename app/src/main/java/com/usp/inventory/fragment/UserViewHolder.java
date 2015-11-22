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
    private String uid;

    public UserViewHolder(View userView) {
        super(userView);
        ButterKnife.bind(this, userView);
        userView.setOnClickListener(onClickListener);
    }

    public void setUser(User user, String uid) {
        this.user = user;
        this.uid = uid;
        if (user.getUid().equals(uid)) {
            //view.setVisibility(View.GONE);
            nameTextView.setText("");
            emailTextView.setText("me");

        } else {
            nameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());
        }


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (user.getUid().equals(uid)) {
                // Do nothing
            } else {
                v.getContext().startActivity(InventoryActivity.getIntent(v.getContext(), user));
            }
        }
    };
}
