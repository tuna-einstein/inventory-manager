package com.usp.inventory;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.usp.inventory.activity.PlaceOrderActivity;
import com.usp.inventory.model.Item;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by umasankar on 11/12/15.
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.thumbnail)
    public ImageView thumbnail;

    @Bind(R.id.name)
    public TextView name;

    @Bind(R.id.description1)
    public TextView description1;

    @Bind(R.id.units)
    public TextView units;


    private Item item;
    private String uid;

    TextView messageText;
    TextView nameText;

    public ItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(onClickListener);
    }

    public void setItem(Item item, String uid) {
        this.item = item;
        name.setText(item.getName());
        description1.setText(item.getDescription1().toString());
        units.setText("Available : " + item.getUnits());
        this.uid = uid;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (item.getOwnerId().equals(uid)) {
                // Show the update screen.
            } else {
                v.getContext().startActivity(PlaceOrderActivity.getIntent(v.getContext(), item));
            }
        }
    };
}