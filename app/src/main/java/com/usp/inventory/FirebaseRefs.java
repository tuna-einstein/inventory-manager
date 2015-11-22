package com.usp.inventory;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

/**
 * Created by umasankar on 11/15/15.
 */
public class FirebaseRefs {

    private Firebase firebaseRootRef;

    public FirebaseRefs(Firebase firebaseRootRef) {
        this.firebaseRootRef = firebaseRootRef;
    }

    public Firebase getItemsRef() {
        return firebaseRootRef.child("items");
    }

    public Firebase getOrdersRef() {
        return firebaseRootRef.child("orders");
    }

    public Query getMyOrdersQuery(String uid) {
        return getOrdersRef()
                .orderByChild("requesterId")
                .startAt(uid)
                .endAt(uid);
    }

    public Query getMyApprovalsQuery(String uid) {
        return getOrdersRef()
                .orderByChild("itemOwnerId")
                .startAt(uid)
                .endAt(uid);
    }

    public Query getMyItemsQuery(String uid) {
        return getItemsRef()
                .orderByChild("ownerId")
                .startAt(uid)
                .endAt(uid);
    }

    public Firebase getFirebaseOutgoingRef(String uid) {
        return firebaseRootRef.child("outgoing").child(uid);
    }

    public Firebase getFirebaseIncomingRef(String uid) {
        return firebaseRootRef.child("incoming").child(uid);
    }

    public Firebase getHistoryOutgoingRef(String uid) {
        return firebaseRootRef.child("history-outgoing").child(uid);
    }

    public Firebase getHistoryIncomingRef(String uid) {
        return firebaseRootRef.child("history-incoming").child(uid);
    }

    public String getOutgoingPath(String uid, String itemId) {
        return "outgoing/" + uid + "/" + itemId;
    }

    public String getIncomingPath(String uid, String itemId) {
        return "incoming/" + uid + "/" + itemId;
    }

    public Firebase getFirebaseUsersRef() {
        return firebaseRootRef.child("users");
    }

    public Firebase getFirebaseRootRef() {
        return firebaseRootRef;
    }
}
