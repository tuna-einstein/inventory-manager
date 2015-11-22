package com.usp.inventory.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

import com.firebase.ui.FirebaseRecyclerViewAdapter;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.usp.inventory.R;
import com.usp.inventory.activity.AddItemActivity;
import com.usp.inventory.model.Item;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListItemFragment extends BaseFragment {

    private static final String UID_KEY = "uid";

    private static final String SEARCH_PREFIX = "search_prefix";

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.add_item)
    FloatingActionButton addItem;

    FirebaseRecyclerViewAdapter adapter;

    @Bind(R.id.stub)
    ViewStub viewStub;

    @Inject
    ItemsAdapter itemsAdapter;

    public ListItemFragment() {
        // Required empty public constructor
    }

    public static ListItemFragment getInstance(String uid, String searchPrefix) {
        ListItemFragment fragment = new ListItemFragment();
        Bundle args = new Bundle();
        args.putString(UID_KEY, uid);
        args.putString(SEARCH_PREFIX, searchPrefix);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        String uid = args.getString(UID_KEY, "");
        final String searchPrefix = args.getString(SEARCH_PREFIX);
        if (!uid.equals(sharedPreferencesStore.getUid())) {
            addItem.setVisibility(View.INVISIBLE);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (sharedPreferencesStore.getUid().equals(uid)) {
            recyclerView.setAdapter(itemsAdapter);
        } else if (Strings.isNullOrEmpty(uid)) {
            adapter = new FirebaseRecyclerViewAdapter<Item, ItemViewHolder>(
                    Item.class, R.layout.list_row, ItemViewHolder.class,
                    firebaseRefs.getItemsRef().orderByChild("name")
                            .startAt(searchPrefix).limitToFirst(30)) {

                @Override
                public void populateViewHolder(ItemViewHolder itemViewHolder, Item item) {
                    itemViewHolder.setItem(item, sharedPreferencesStore.getUid());
                }
            };

            recyclerView.setAdapter(adapter);

        } else {
            adapter = new FirebaseRecyclerViewAdapter<Item, ItemViewHolder>(
                    Item.class, R.layout.list_row, ItemViewHolder.class,
                    firebaseRefs.getMyItemsQuery(uid)) {

                @Override
                public void populateViewHolder(ItemViewHolder itemViewHolder, Item item) {
                    itemViewHolder.setItem(item, sharedPreferencesStore.getUid());
                }
            };
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_list_item;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle data) {
        if (mListener != null) {
            mListener.onFragmentInteraction(data);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (adapter != null) {
            adapter.cleanup();
        }
    }

    @OnClick(R.id.add_item)
    public void onAddItemClick() {
        startActivity(AddItemActivity.getIntent(getActivity(), null));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
