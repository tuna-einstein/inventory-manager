package com.usp.inventory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.usp.inventory.R;
import com.usp.inventory.fragment.ListItemFragment;
import com.usp.inventory.model.User;

public class InventoryActivity extends BaseActivity {

    private static final String USER_ID_KEY = "user_id";
    private static final String DISPLAY_NAME_KEY = "display_name_key";
    private static final String EMAIL_KEY = "email_key";

    private ActionBar actionBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uid = getIntent().getStringExtra(USER_ID_KEY);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra(DISPLAY_NAME_KEY));
        actionBar.setSubtitle(getIntent().getStringExtra(EMAIL_KEY));

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // toolbar.set

        //User user = gson.fromJson(getIntent().getStringExtra(USER_KEY), User.class);
        replaceFragment(ListItemFragment.getInstance(uid));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_inventory;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent(Context context, User user) {
        Intent intent = new Intent(context, InventoryActivity.class);
        intent.putExtra(USER_ID_KEY, user.getUid());
        intent.putExtra(DISPLAY_NAME_KEY, user.getDisplayName());
        intent.putExtra(EMAIL_KEY, user.getEmail());
        return intent;
    }
}
