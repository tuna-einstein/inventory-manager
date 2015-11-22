package com.usp.inventory;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import javax.inject.Inject;

/**
 * Created by umasankar on 11/19/15.
 */
public class DataChangeListener implements ValueEventListener {

    private FirebaseRefs firebaseRefs;
    private SharedPreferencesStore sharedPrefsStore;
    private OnChangedListener onChangedListener;

    @Inject
    public DataChangeListener(FirebaseRefs firebaseRefs,
                              SharedPreferencesStore sharedPrefsStore) {
        this.firebaseRefs = firebaseRefs;
        this.sharedPrefsStore = sharedPrefsStore;
    }

    public void startListening(OnChangedListener listener) {
        this.onChangedListener = listener;
        firebaseRefs.getItemsRef().addValueEventListener(this);
        firebaseRefs.getFirebaseIncomingRef(sharedPrefsStore.getUid()).addValueEventListener(this);
        firebaseRefs.getHistoryOutgoingRef(sharedPrefsStore.getUid()).addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        onChangedListener.onChanged(
                OnChangedListener.EventType.Changed, dataSnapshot, findDataType(dataSnapshot));

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    public interface OnChangedListener {
        enum EventType { Added, Changed, Removed, Moved }

        enum DataType { UNKNOWN, INCOMING_ORDER, HISTORY_OUTGOING_ORDER, ITEM }

        void onChanged(EventType eventType, DataSnapshot dataSnapshot, DataType dataType);
    }

    private OnChangedListener.DataType findDataType(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getRef().getPath().equals(
                firebaseRefs.getFirebaseIncomingRef(sharedPrefsStore.getUid()).getPath())) {
            return OnChangedListener.DataType.INCOMING_ORDER;
        }


        if (dataSnapshot.getRef().getPath().equals(
                firebaseRefs.getHistoryOutgoingRef(sharedPrefsStore.getUid()).getPath())) {
            return OnChangedListener.DataType.HISTORY_OUTGOING_ORDER;
        }

        if (dataSnapshot.getRef().getPath().equals(
                firebaseRefs.getItemsRef().getPath())) {
            return OnChangedListener.DataType.ITEM;
        }

        return OnChangedListener.DataType.UNKNOWN;
    }
}