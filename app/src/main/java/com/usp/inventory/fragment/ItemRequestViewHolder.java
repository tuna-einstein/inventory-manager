package com.usp.inventory.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.usp.inventory.FirebaseRefs;
import com.usp.inventory.R;
import com.usp.inventory.activity.AcceptOrderActivity;
import com.usp.inventory.activity.PlaceOrderActivity;
import com.usp.inventory.model.Item;
import com.usp.inventory.model.ItemRequest;
import com.usp.inventory.model.User;

import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by umasankar on 11/17/15.
 */
public class ItemRequestViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_name)
    public TextView itemNameTextView;

    @Bind(R.id.description1)
    public TextView descriptionTextView;

    @Bind(R.id.description1_label)
    public TextView descriptionLabel;

    @Bind(R.id.units)
    public TextView unitsTextView;

    @Bind(R.id.status)
    public TextView statusTextView;

    @Bind(R.id.date)
    public TextView dateTextView;


    private ItemRequest itemRequest;
    private FirebaseRefs firebaseRefs;

    public ItemRequestViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        view.setOnClickListener(onClickListener);
    }

    private Item item;
    private User requester;
    private User itemOwner;
    private ItemRequestAdapter.Type type;

    public void setData(ItemRequest itemRequest,
                        ItemRequestAdapter.Type type,
                        FirebaseRefs firebaseRefs) {
        this.itemRequest = itemRequest;
        unitsTextView.setText(String.valueOf(itemRequest.getUnits()));
        this.firebaseRefs = firebaseRefs;
        this.type = type;
        setDate();
        fetchItem();


        if (type == ItemRequestAdapter.Type.APPROVAL) {
            descriptionLabel.setText("Requester : ");
            fetchRequesterDetails();
        }
        if (type == ItemRequestAdapter.Type.ORDER) {
            descriptionLabel.setText("Owner : ");
            fetchItemOwnerDetails();
        }

        switch (itemRequest.getStatus()) {
            case 0 : statusTextView.setText(
                    Html.fromHtml("status : " + getHtmlText("pending", "red")));
                break;
            case 1 : statusTextView.setText(
                    Html.fromHtml("status : " + getHtmlText("accepted", "green")));
                break;
            case 2 : statusTextView.setText(
                    Html.fromHtml("status : " + getHtmlText("rejected", "blue")));
                break;
        }
    }

    private String getHtmlText(String text, String color) {
        return String.format("<font color='%s'><b> %s </b> </font>", color, text);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (type == ItemRequestAdapter.Type.APPROVAL
                    && itemRequest.getStatus() == 0) {
                v.getContext().startActivity(
                        AcceptOrderActivity.getIntent(v.getContext(), item, requester, itemRequest));
            } else if (type == ItemRequestAdapter.Type.ORDER
                    && itemRequest.getStatus() == 0) {
                v.getContext().startActivity(
                        PlaceOrderActivity.getIntent(v.getContext(), item, itemRequest));
            }
        }
    };

    private void fetchItem() {
        firebaseRefs.getItemsRef().child(itemRequest.getItemId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        item = snapshot.getValue(Item.class);
                        if (item.getId().equals(itemRequest.getItemId())) {
                            itemNameTextView.setText(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
    }

    private void fetchRequesterDetails() {
        firebaseRefs.getFirebaseUsersRef().child(itemRequest.getRequesterId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        requester = snapshot.getValue(User.class);
                        if (requester.getUid().equals(itemRequest.getRequesterId())) {
                            descriptionTextView.setText(requester.getDisplayName());
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
    }


    private void fetchItemOwnerDetails() {
        firebaseRefs.getFirebaseUsersRef().child(itemRequest.getItemOwnerId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        itemOwner = snapshot.getValue(User.class);
                        if (itemOwner.getUid().equals(itemRequest.getItemOwnerId())) {
                            descriptionTextView.setText(itemOwner.getDisplayName());
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
    }

    private void setDate() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(itemRequest.getTimeStamp());
        String date = DateFormat.getMediumDateFormat(dateTextView.getContext()).format(cal.getTime());
        dateTextView.setText(date);
    }
}

