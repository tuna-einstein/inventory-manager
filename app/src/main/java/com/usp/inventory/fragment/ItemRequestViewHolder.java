package com.usp.inventory.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.usp.inventory.FirebaseRefs;
import com.usp.inventory.R;
import com.usp.inventory.activity.AcceptOrderActivity;
import com.usp.inventory.model.Item;
import com.usp.inventory.model.ItemRequest;
import com.usp.inventory.model.User;

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
    private ListRequestFragment.Type type;

    public void setData(ItemRequest itemRequest,
                        ListRequestFragment.Type type,
                        FirebaseRefs firebaseRefs) {
        this.itemRequest = itemRequest;
        unitsTextView.setText(String.valueOf(itemRequest.getUnits()));
        this.firebaseRefs = firebaseRefs;
        this.type = type;
        fetchItem();
        if (type == ListRequestFragment.Type.INCOMING
                || type == ListRequestFragment.Type.HISTORY_INCOMING) {
            descriptionLabel.setText("Requester : ");
            fetchRequesterDetails();
        }
        if (type == ListRequestFragment.Type.OUTGOING
                || type == ListRequestFragment.Type.HISTORY_OUTGOING) {
            descriptionLabel.setText("Owner : ");
            fetchItemOwnerDetails();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (type == ListRequestFragment.Type.INCOMING) {
                v.getContext().startActivity(
                        AcceptOrderActivity.getIntent(v.getContext(), item, requester, itemRequest));
            }
        }
    };

    private void fetchItem() {
        firebaseRefs.getFirebaseItemsRef(itemRequest.getItemOwnerId()).child(itemRequest.getItemId())
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
}

