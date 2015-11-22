package com.usp.inventory.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.usp.inventory.R;
import com.usp.inventory.activity.AddItemActivity;
import com.usp.inventory.activity.MainActivity;
import com.usp.inventory.activity.PlaceOrderActivity;
import com.usp.inventory.event.MessageEvent;
import com.usp.inventory.model.Item;
import com.usp.inventory.model.ItemRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

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

    @Bind(R.id.pending_request)
    public TextView pendingRequests;


    private Item item;
    private String uid;

    public ItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(onClickListener);
        EventBus.getDefault().register(this);
    }


    public void setItem(Item item, String uid) {
        this.item = item;
        name.setText(item.getName());
        description1.setText(item.getDescription1().toString());
        units.setText("Available : " + item.getUnits());
        this.uid = uid;
        updatePendingCount();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (item.getOwnerId().equals(uid)) {
                v.getContext().startActivity(AddItemActivity.getIntent(v.getContext(), item));
            } else {
                v.getContext().startActivity(PlaceOrderActivity.getIntent(v.getContext(), item, null));
            }
        }
    };


    // This method will be called when a MessageEvent is posted
    public void onEvent(MessageEvent event){
        if (event.eventType == MessageEvent.EventType.MY_ORDERS_CHANGED) {
            updatePendingCount();
        }
    }

    private void updatePendingCount() {
        int pendingCount = 0;
        for (ItemRequest request : MainActivity.myOrders.values()) {
            if (request.getStatus() != 0) {
                continue;
            }
            if (!request.getItemId().equals(item.getId())) {
                continue;
            }

            pendingCount += request.getUnits();
        }
        if (pendingCount != 0) {
            pendingRequests.setVisibility(View.VISIBLE);
            pendingRequests.setText("My order : " + pendingCount);
        } else {
            pendingRequests.setVisibility(View.GONE);
        }
    }

}