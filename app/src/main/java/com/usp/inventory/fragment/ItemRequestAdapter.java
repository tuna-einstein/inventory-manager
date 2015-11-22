package com.usp.inventory.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usp.inventory.FirebaseRefs;
import com.usp.inventory.R;
import com.usp.inventory.SharedPreferencesStore;
import com.usp.inventory.event.MessageEvent;
import com.usp.inventory.model.ItemRequest;

import java.util.TreeMap;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

import static com.usp.inventory.event.MessageEvent.*;

/**
 * Created by umasankar on 11/21/15.
 */
public class ItemRequestAdapter extends RecyclerView.Adapter<ItemRequestViewHolder> {

    private TreeMap<String, ItemRequest> data;
    private Type type;

    private FirebaseRefs firebaseRefs;
    private SharedPreferencesStore sharedPreferencesStore;

    @Inject
    public ItemRequestAdapter(FirebaseRefs firebaseRefs,
                              SharedPreferencesStore sharedPreferencesStore) {
        this.firebaseRefs = firebaseRefs;
        this.sharedPreferencesStore = sharedPreferencesStore;
        EventBus.getDefault().register(this);
    }

    public void setDataAndType(TreeMap data, Type type) {
        this.type = type;
        this.data = data;
    }

    @Override
    public ItemRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.request_list_item, parent, false);
        return new ItemRequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRequestViewHolder holder, int position) {
        holder.setData(getItemRequest(position), type, firebaseRefs);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    private ItemRequest getItemRequest(int index) {
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
        if (event.eventType == EventType.MY_APPROVALS_CHANGED
                && type == Type.APPROVAL) {
            // Optimize this
            notifyDataSetChanged();
        }
        if (event.eventType == EventType.MY_ORDERS_CHANGED
                && type == Type.ORDER) {
            // Optimize this
            notifyDataSetChanged();
        }
    }

    public static enum Type {
        APPROVAL,
        ORDER
    }


}
