package com.usp.inventory.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.usp.inventory.FirebaseRefs;
import com.usp.inventory.ItemApiRx;
import com.usp.inventory.R;
import com.usp.inventory.model.Item;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class AddItemActivity extends BaseActivity {

    private static final String ITEM_KEY = "item";

    @Bind(R.id.txt_description1) EditText description1;
    @Bind(R.id.txt_description2) EditText description2;
    @Bind(R.id.txt_item_name) EditText name;
    @Bind(R.id.editText_enter_units) EditText units;
    @Bind(R.id.button_add) Button addbutton;

    @Inject
    ItemApiRx itemApiRx;
    @Inject ProgressDialog progressDialog;
    @Inject
    FirebaseRefs firebaseRefs;

    private Item item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getIntent().getParcelableExtra(ITEM_KEY);
        if (item != null) {
            setTitle("Update item");
            description1.setText(item.getDescription1());
            description2.setText(item.getDescription2());
            name.setText(item.getName());
            units.setText(String.valueOf(item.getUnits()));
            addbutton.setText("Update");
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_item;
    }

    @OnClick(R.id.button_add)
    public void submit() {
        if (item == null) {
            item = new Item();
        }
        item.setDescription1(description1.getText().toString());
        item.setDescription2(description2.getText().toString());
        item.setName(name.getText().toString().toLowerCase());
        item.setUnits(Integer.valueOf(units.getText().toString()));
        item.setOwnerId(sharedPreferencesStore.getUid());
        Firebase pushRef;
        if (Strings.isNullOrEmpty(item.getId())) {
            pushRef = firebaseRefs.getItemsRef().push();
            item.setId(pushRef.getKey());
        } else {
            pushRef =  firebaseRefs.getItemsRef().child(item.getId());
        }
        pushRef.setValue(item);
        finish();
//
//
//        itemApiRx.addItem(item)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Item>() {
//                    @Override
//                    public void onCompleted() {
//                        progressDialog.dismiss();
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Snackbar.make(units, "Add item failed: " + e.toString(), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                        progressDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onNext(Item item) {
//                        progressDialog.dismiss();
//                        finish();
//                    }
//                });
    }

    public static Intent getIntent(Context context, Item item) {
        Intent intent = new Intent(context, AddItemActivity.class);
        intent.putExtra(ITEM_KEY, item);
        return intent;
    }
}
