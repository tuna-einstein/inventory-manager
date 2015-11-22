package com.usp.inventory.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usp.inventory.R;
import com.usp.inventory.SharedPreferencesStore;
import com.usp.inventory.activity.MainActivity;
import com.usp.inventory.event.MessageEvent;
import com.usp.inventory.model.Item;

import java.util.TreeMap;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by umasankar on 11/21/15.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private TreeMap<String, Item> data;

    private SharedPreferencesStore sharedPreferencesStore;

    @Inject
    public ItemsAdapter(SharedPreferencesStore sharedPreferencesStore) {
        this.sharedPreferencesStore = sharedPreferencesStore;
        EventBus.getDefault().register(this);
        data = MainActivity.myItems;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_row, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.setItem(getItemRequest(position), sharedPreferencesStore.getUid());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    private Item getItemRequest(int index) {
        int count = 0;
        for (String k : data.keySet()) {
            if (count == index) {
                return data.get(k);
            }
            count ++;
        }
        throw new IllegalArgumentException("Invalid index " + index);
    }

    // This method will be called when a MessageEvent is posted
    public void onEvent(MessageEvent event){
        if (event.eventType == MessageEvent.EventType.MY_ITEMS_CHANGED) {
            // Optimize this
            notifyDataSetChanged();
        }
    }
}

