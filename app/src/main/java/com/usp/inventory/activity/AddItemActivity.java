package com.usp.inventory.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.usp.inventory.FirebaseRefs;
import com.usp.inventory.ItemApiRx;
import com.usp.inventory.R;
import com.usp.inventory.model.Item;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class AddItemActivity extends BaseActivity {

    @Bind(R.id.txt_description1) EditText description1;
    @Bind(R.id.txt_description2) EditText description2;
    @Bind(R.id.txt_item_name) EditText name;
    @Bind(R.id.editText_enter_units) EditText units;

    @Inject
    ItemApiRx itemApiRx;
    @Inject ProgressDialog progressDialog;
    @Inject
    FirebaseRefs firebaseRefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_item;
    }

    @OnClick(R.id.button_add)
    public void submit() {
        Item item = new Item();
        item.setDescription1(description1.getText().toString());
        item.setDescription2(description2.getText().toString());
        item.setName(name.getText().toString());
        item.setUnits(Integer.valueOf(units.getText().toString()));
        item.setOwnerId(sharedPreferencesStore.getUid());
        Firebase pushRef = firebaseRefs.getFirebaseItemsRef(sharedPreferencesStore.getUid()).push();
        item.setId(pushRef.getKey());
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
}
