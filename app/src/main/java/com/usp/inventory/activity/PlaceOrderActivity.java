package com.usp.inventory.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.usp.inventory.R;
import com.usp.inventory.model.Item;
import com.usp.inventory.model.ItemRequest;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by umasankar on 11/16/15.
 */
public class PlaceOrderActivity extends BaseActivity {

    private static final String ITEM_ID = "item_id";
    private static final String ITEM_DESC1_KEY = "desc1";
    private static final String ITEM_DESC2_KEY = "desc2";
    private static final String ITEM_NAME_KEY = "name";
    private static final String ITEM_OWNER_ID_KEY = "owner_id";

    @Bind(R.id.txt_description1) TextView description1;
    @Bind(R.id.txt_description2) TextView description2;
    @Bind(R.id.txt_item_name) TextView itemNameTextView;
    @Bind(R.id.editText_enter_units) EditText units;
    @Bind(R.id.button_place_order)
    Button orderButton;


    private String ownerId;
    private String itemId;
    private String itemName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemId = getIntent().getStringExtra(ITEM_ID);
        String desc1 = getIntent().getStringExtra(ITEM_DESC1_KEY);
        String desc2 = getIntent().getStringExtra(ITEM_DESC2_KEY);
        itemName = getIntent().getStringExtra(ITEM_NAME_KEY);
        ownerId = getIntent().getStringExtra(ITEM_OWNER_ID_KEY);

        description1.setText(desc1);
        description2.setText(desc2);
        itemNameTextView.setText(itemName);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_place_order;
    }

    public static Intent getIntent(Context context, Item item) {
        Intent intent = new Intent(context, PlaceOrderActivity.class);
        intent.putExtra(ITEM_ID, item.getId());
        intent.putExtra(ITEM_NAME_KEY, item.getName());
        intent.putExtra(ITEM_DESC1_KEY, item.getDescription1());
        intent.putExtra(ITEM_DESC2_KEY, item.getDescription2());
        intent.putExtra(ITEM_OWNER_ID_KEY, item.getOwnerId());
        return intent;
    }

    @OnClick(R.id.button_place_order)
    public void onPlaceOrder() {
        final ItemRequest itemRequest = new ItemRequest();
        itemRequest.setItemOwnerId(ownerId);
        itemRequest.setRequesterId(sharedPreferencesStore.getUid());
        itemRequest.setItemId(itemId);
        itemRequest.setUnits(Integer.valueOf(units.getText().toString()));

        Firebase ref = firebaseRefs.getOrdersRef().push();
        itemRequest.setId(ref.getKey());

        setTitle("Please wait....");
        orderButton.setEnabled(false);

        ref.setValue(itemRequest, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                finish();
            }
        });

//        Map<String, Object> itemRequest = new HashMap<String, Object>();
//        itemRequest.put("itemOwnerId", ownerId);
//        itemRequest.put("itemId", itemId);
//        itemRequest.put("requesterId", sharedPreferencesStore.getUid());
//        itemRequest.put("units", Integer.valueOf(units.getText().toString()));
//        itemRequest.put("status", 0);
//
//        Map<String, Object> updatedUserData = new HashMap<String, Object>();
//        updatedUserData.put(firebaseRefs.getOutgoingPath(
//                sharedPreferencesStore.getUid(), itemId), itemRequest);
//        updatedUserData.put(firebaseRefs.getIncomingPath(ownerId, itemId), itemRequest);
//        firebaseRefs.getFirebaseRootRef().updateChildren(updatedUserData);
    }
}
