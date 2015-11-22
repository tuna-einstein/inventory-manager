package com.usp.inventory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.usp.inventory.R;
import com.usp.inventory.model.Item;
import com.usp.inventory.model.ItemRequest;
import com.usp.inventory.model.User;

import butterknife.Bind;
import butterknife.OnClick;

public class AcceptOrderActivity extends BaseActivity {

    private static final String ITEM_KEY = "item";
    private static final String REQUESTER_EMAIL_KEY = "email";
    private static final String REQUESTER_NAME = "requester_name";

    private static final String ITEM_REQUEST_KEY = "item_request";


    @Bind(R.id.txt_item_name) TextView itemNameTextView;
    @Bind(R.id.txt_units) TextView unitsTextView;
    @Bind(R.id.txt_requester_email) TextView requesterEmailTextView;
    @Bind(R.id.txt_requester_name) TextView requesterNameTextView;

    private ItemRequest itemRequest;
    private Item item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemRequest = getIntent().getParcelableExtra(ITEM_REQUEST_KEY);
        item = getIntent().getParcelableExtra(ITEM_KEY);

        itemNameTextView.setText("Item : " + item.getName());
        unitsTextView.setText("Units requested : " + itemRequest.getUnits());
        requesterEmailTextView.setText(getIntent().getStringExtra(REQUESTER_EMAIL_KEY));
        requesterNameTextView.setText(getIntent().getStringExtra(REQUESTER_NAME));

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_accept_order;
    }

    public static Intent getIntent(
            Context context, Item item, User requester, ItemRequest itemRequest) {
        Intent intent =  new Intent(context, AcceptOrderActivity.class);
        if (item != null) {
            intent.putExtra(ITEM_KEY, item);
        }
        if (requester != null) {
            intent.putExtra(REQUESTER_NAME, requester.getDisplayName());
            intent.putExtra(REQUESTER_EMAIL_KEY, requester.getEmail());
        }
        intent.putExtra(ITEM_REQUEST_KEY, itemRequest);
        return intent;
    }

    @OnClick(R.id.button_accept)
    public void onAccept() {

        // Update num of units
        Firebase itemRef =
                firebaseRefs.getItemsRef().child(itemRequest.getItemId());
        itemRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() == null) {
                    Log.w("FIREBASE", "No data found");
                } else {
                    Item existing = currentData.getValue(Item.class);
                    existing.setUnits(existing.getUnits() - itemRequest.getUnits());
                    currentData.setValue(existing);
                }
                return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    System.out.println("Data could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
                finish();
            }
        });

        updateOrder(1);
    }

    @OnClick(R.id.button_reject)
    public void onReject() {
        updateOrder(2);
        finish();
    }

    private void updateOrder(int status) {
        itemRequest.setStatus(status);
        firebaseRefs.getOrdersRef().child(itemRequest.getId()).setValue(itemRequest);
    }
}
