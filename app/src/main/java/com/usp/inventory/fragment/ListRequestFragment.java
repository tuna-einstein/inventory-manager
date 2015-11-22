package com.usp.inventory.fragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

import com.firebase.client.Query;
import com.usp.inventory.R;
import com.usp.inventory.activity.MainActivity;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListRequestFragment extends BaseFragment {
    private static final String ARG_TYPE = "type";

    private ItemRequestAdapter.Type type;

    private OnFragmentInteractionListener listener;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.stub)
    ViewStub viewStub;

    @Inject
    ItemRequestAdapter adapter;

    public static ListRequestFragment newInstance(ItemRequestAdapter.Type type) {
        ListRequestFragment fragment = new ListRequestFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public ListRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (ItemRequestAdapter.Type) getArguments().getSerializable(ARG_TYPE);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query ref = null;
        switch (type) {
            case APPROVAL:
                //ref = firebaseRefs.getMyApprovalsQuery(sharedPreferencesStore.getUid());
                adapter.setDataAndType(MainActivity.myApprovals, type);
                break;
            case ORDER:
                //ref = firebaseRefs.getMyOrdersQuery(sharedPreferencesStore.getUid());
                adapter.setDataAndType(MainActivity.myOrders, type);
                break;
            default:
                throw new RuntimeException("Invalid type for fragment");
        }
//        adapter = new FirebaseRecyclerViewAdapter<ItemRequest, ItemRequestViewHolder>(
//                ItemRequest.class, R.layout.request_list_item, ItemRequestViewHolder.class, ref) {
//
//            @Override
//            public void populateViewHolder(ItemRequestViewHolder viewHolder, ItemRequest data) {
//                viewHolder.setData(data, type, firebaseRefs);
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                checkEmpty();
//                return super.getItemViewType(position);
//            }
//        };

        recyclerView.setAdapter(adapter);
        checkEmpty();
    }

    private void checkEmpty() {
        if (adapter.getItemCount() == 0) {
            viewStub.setVisibility(View.VISIBLE);
        } else {
            viewStub.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_list_request;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
       // adapter.cleanup();
    }

}
