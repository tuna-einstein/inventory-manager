package com.usp.inventory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usp.inventory.fragment.ItemViewHolder;
import com.usp.inventory.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by umasankar on 11/20/15.
 */
public class SearchItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private final Context context;

    private List<Item> data = new ArrayList<>();

    public SearchItemAdapter(Context context) {
        this.context = context;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.setItem(data.get(position), null);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(Item s, int position) {
        position = position == -1 ? getItemCount()  :  position;
        data.add(position, s);
        notifyItemInserted(position);
    }

    public void remove(int position){
        if (position < getItemCount()  ) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
}
