package com.usp.inventory.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.usp.inventory.DataChangeListener;
import com.usp.inventory.DataChangeListener.OnChangedListener;
import com.usp.inventory.R;
import com.usp.inventory.event.MessageEvent;
import com.usp.inventory.fragment.ItemRequestAdapter;
import com.usp.inventory.fragment.ListRequestFragment;
import com.usp.inventory.fragment.ListItemFragment;
import com.usp.inventory.fragment.MarketFragment;
import com.usp.inventory.model.Item;
import com.usp.inventory.model.ItemRequest;

import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

import static com.usp.inventory.event.MessageEvent.*;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChildEventListener {

    @Bind(R.id.displayname) TextView displayNameTextView;
    @Bind(R.id.email) TextView emailTextView;
    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.nav_view) NavigationView navigationView;

    public static TreeMap<String, ItemRequest> myOrders = new TreeMap<>();
    public static TreeMap<String, ItemRequest> myApprovals = new TreeMap<>();
    public static TreeMap<String, Item> myItems = new TreeMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        EventBus.getDefault().register(this);
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

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                replaceFragment(ListItemFragment.getInstance(null, query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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
            replaceFragment(ListItemFragment.getInstance(sharedPreferencesStore.getUid(), null));
            setTitle(R.string.nav_my_items);
        } else if (id == R.id.nav_market) {
            Fragment fragment = new MarketFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            replaceFragment(fragment);
            setTitle(R.string.nav_market);
        } else if (id == R.id.nav_my_approvals) {
            replaceFragment(ListRequestFragment.newInstance(ItemRequestAdapter.Type.APPROVAL));
            setTitle(R.string.nav_my_approvals);
        } else if (id == R.id.nav_my_orders) {
            replaceFragment(ListRequestFragment.newInstance(ItemRequestAdapter.Type.ORDER));
            setTitle(R.string.nav_my_orders);
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
        replaceFragment(ListItemFragment.getInstance(sharedPreferencesStore.getUid(), null));
        setTitle(R.string.nav_my_items);
        listenToFirebaseDataChange();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    // This method will be called when a MessageEvent is posted
    public void onEvent(MessageEvent event){
        appendInfoToMenu(R.id.nav_my_items,
                getString(R.string.nav_my_items)
                        + "  (" + String.valueOf(myItems.size()) + ")");
        appendInfoToMenu(R.id.nav_my_approvals,
                getString(R.string.nav_my_approvals)
                        + "  (" + String.valueOf(getPendingApprovalCount()) + ")");
        appendInfoToMenu(R.id.nav_my_orders,
                getString(R.string.nav_my_orders)
                        + "  (" + String.valueOf(getPendingOrdersCount()) + ")");
    }

    private void appendInfoToMenu(int menuId, String info) {
        Menu menuNav = navigationView.getMenu();
        MenuItem element = menuNav.findItem(menuId);
        SpannableString sColored = new SpannableString( info );
        //sColored.setSpan(new BackgroundColorSpan(Color.GRAY), info.length() - 5, info.length(), 0);
        //sColored.setSpan(new ForegroundColorSpan(Color.RED), info.length() - 5, info.length(), 0);
        element.setTitle(sColored);

    }



    private void listenToFirebaseDataChange() {
        firebaseRefs.getMyOrdersQuery(sharedPreferencesStore.getUid()).addChildEventListener(this);
        firebaseRefs.getMyApprovalsQuery(sharedPreferencesStore.getUid()).addChildEventListener(this);
        firebaseRefs.getMyItemsQuery(sharedPreferencesStore.getUid()).addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
        int index = 0;
        if (isOrdersSnapshot(dataSnapshot)) {
            ItemRequest itemRequest = dataSnapshot.getValue(ItemRequest.class);
            if (itemRequest.getRequesterId().equals(sharedPreferencesStore.getUid())) {
                myOrders.put(dataSnapshot.getKey(), itemRequest);
                EventBus.getDefault().post(new MessageEvent(EventType.MY_ORDERS_CHANGED));
            } else {
                myApprovals.put(dataSnapshot.getKey(), itemRequest);
                EventBus.getDefault().post(new MessageEvent(EventType.MY_APPROVALS_CHANGED));
            }
        }

        if (isItemsSnapshot(dataSnapshot)) {
            myItems.put(dataSnapshot.getKey(), dataSnapshot.getValue(Item.class));
            EventBus.getDefault().post(new MessageEvent(EventType.MY_ITEMS_CHANGED));
        }

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        if (isOrdersSnapshot(dataSnapshot)) {
            ItemRequest itemRequest = dataSnapshot.getValue(ItemRequest.class);
            if (itemRequest.getRequesterId().equals(sharedPreferencesStore.getUid())) {
                myOrders.put(dataSnapshot.getKey(), itemRequest);
                EventBus.getDefault().post(new MessageEvent(EventType.MY_ORDERS_CHANGED));
            } else {
                myApprovals.put(dataSnapshot.getKey(), itemRequest);
                EventBus.getDefault().post(new MessageEvent(EventType.MY_APPROVALS_CHANGED));
            }
        }

        if (isItemsSnapshot(dataSnapshot)) {
            myItems.put(dataSnapshot.getKey(), dataSnapshot.getValue(Item.class));
            EventBus.getDefault().post(new MessageEvent(EventType.MY_ITEMS_CHANGED));
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        if (isOrdersSnapshot(dataSnapshot)) {
            ItemRequest itemRequest = dataSnapshot.getValue(ItemRequest.class);
            if (itemRequest.getRequesterId().equals(sharedPreferencesStore.getUid())) {
                myOrders.remove(dataSnapshot.getKey());
                EventBus.getDefault().post(new MessageEvent(EventType.MY_ORDERS_CHANGED));
            } else {
                myApprovals.remove(dataSnapshot.getKey());
                EventBus.getDefault().post(new MessageEvent(EventType.MY_APPROVALS_CHANGED));
            }
        }


        if (isItemsSnapshot(dataSnapshot)) {
            myItems.remove(dataSnapshot.getKey());
            EventBus.getDefault().post(new MessageEvent(EventType.MY_ITEMS_CHANGED));
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        if (isOrdersSnapshot(dataSnapshot)) {
            ItemRequest itemRequest = dataSnapshot.getValue(ItemRequest.class);
            if (itemRequest.getRequesterId().equals(sharedPreferencesStore.getUid())) {
                myOrders.remove(s);
                myOrders.put(dataSnapshot.getKey(), itemRequest);
                EventBus.getDefault().post(new MessageEvent(EventType.MY_ORDERS_CHANGED));
            } else {
                myApprovals.remove(s);
                myApprovals.put(dataSnapshot.getKey(), itemRequest);
                EventBus.getDefault().post(new MessageEvent(EventType.MY_APPROVALS_CHANGED));
            }
        }

        if (isItemsSnapshot(dataSnapshot)) {
            myItems.remove(s);
            myItems.put(dataSnapshot.getKey(), dataSnapshot.getValue(Item.class));
            EventBus.getDefault().post(new MessageEvent(EventType.MY_ITEMS_CHANGED));
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    private boolean isOrdersSnapshot(DataSnapshot snapshot) {
        return snapshot.getRef().getParent().getPath().equals(
                firebaseRefs.getOrdersRef().getPath());
    }

    private boolean isItemsSnapshot(DataSnapshot snapshot) {
        return snapshot.getRef().getParent().getPath().equals(
                firebaseRefs.getItemsRef().getPath());
    }

    private int getPendingApprovalCount() {
        int count = 0;
        for (ItemRequest request : myApprovals.values()) {
            if (request.getStatus() == 0) {
                count++;
            }
        }
        return count;
    }

    private int getPendingOrdersCount() {
        int count = 0;
        for (ItemRequest request : myOrders.values()) {
            if (request.getStatus() == 0) {
                count++;
            }
        }
        return count;
    }
}
