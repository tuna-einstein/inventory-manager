package com.usp.inventory.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.FirebaseRecyclerViewAdapter;
import com.usp.inventory.R;
import com.usp.inventory.model.User;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MarketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;
    FirebaseRecyclerViewAdapter adapter;


    public MarketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FirebaseRecyclerViewAdapter<User, UserViewHolder>(
                User.class, R.layout.user_list_row, UserViewHolder.class,
                firebaseRefs.getFirebaseUsersRef()) {

            @Override
            public void populateViewHolder(UserViewHolder userViewHolder, User user) {
                userViewHolder.setUser(user, sharedPreferencesStore.getUid());
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_market;
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
    }
}
