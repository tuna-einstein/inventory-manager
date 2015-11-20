package com.usp.inventory.activity;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.usp.inventory.R;
import com.usp.inventory.fragment.ListRequestFragment;
import com.usp.inventory.fragment.ListItemFragment;
import com.usp.inventory.fragment.MarketFragment;

import butterknife.Bind;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.displayname) TextView displayNameTextView;
    @Bind(R.id.email) TextView emailTextView;
    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.nav_view) NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        Menu menuNav = navigationView.getMenu();
        MenuItem element = menuNav.findItem(R.id.nav_approval);
        String before = element.getTitle().toString();

        String counter = Integer.toString(5);
        String s = before + "   "+counter+" ";
        SpannableString sColored = new SpannableString( s );

        sColored.setSpan(new BackgroundColorSpan(Color.GRAY), s.length() - 3, s.length(), 0);
        sColored.setSpan(new ForegroundColorSpan(Color.RED), s.length() - 3, s.length(), 0);


        element.setTitle(sColored);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_items) {
            replaceFragment(ListItemFragment.getInstance(sharedPreferencesStore.getUid()));
            setTitle(R.string.nav_my_items);
        } else if (id == R.id.nav_market) {
            Fragment fragment = new MarketFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            replaceFragment(fragment);
            setTitle(R.string.nav_market);
        } else if (id == R.id.nav_approval) {
            replaceFragment(ListRequestFragment.newInstance(ListRequestFragment.Type.INCOMING));
            setTitle(R.string.nav_approval);
        } else if (id == R.id.nav_pending_orders) {
            replaceFragment(ListRequestFragment.newInstance(ListRequestFragment.Type.OUTGOING));
            setTitle(R.string.nav_pending_orders);
        } else if (id == R.id.nav_approved) {
            replaceFragment(ListRequestFragment.newInstance(ListRequestFragment.Type.HISTORY_INCOMING));
            setTitle(R.string.nav_approved);
        } else if (id == R.id.nav_my_old_requests) {
            replaceFragment(ListRequestFragment.newInstance(ListRequestFragment.Type.HISTORY_OUTGOING));
            setTitle(R.string.nav_orders_history);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
       // ItemApi.Builder
        return true;
    }

    @Override
    protected void onFirebaseSetup() {
        super.onFirebaseSetup();
        displayNameTextView.setText(sharedPreferencesStore.getDisplayName());
        emailTextView.setText(sharedPreferencesStore.getAccountName());
        navigationView.getMenu().getItem(0).setChecked(true);
        replaceFragment(ListItemFragment.getInstance(sharedPreferencesStore.getUid()));
        setTitle(R.string.nav_my_items);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }
}
