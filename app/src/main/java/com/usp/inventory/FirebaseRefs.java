package com.usp.inventory;

import com.firebase.client.Firebase;

import javax.inject.Inject;

/**
 * Created by umasankar on 11/15/15.
 */
public class FirebaseRefs {

    private Firebase firebaseRootRef;

    public FirebaseRefs(Firebase firebaseRootRef) {
        this.firebaseRootRef = firebaseRootRef;
    }

    public Firebase getFirebaseItemsRef(String uid) {
        return firebaseRootRef.child("items").child(uid);
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
