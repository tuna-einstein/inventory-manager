package com.usp.inventory;

import android.accounts.Account;
import android.app.Activity;
import android.app.Application;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.Scopes;
import com.usp.inventory.backend.itemApi.model.Item;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by umasankar on 11/14/15.
 */
public class Rx {

    String scope = "oauth2:" + Scopes.EMAIL;
    Application application;

    @Inject
    public Rx(Application application) {
        this.application = application;
    }

    public Observable<String> getAuthToken(final Account account) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String token = GoogleAuthUtil.getToken(application, account, scope);
                    subscriber.onNext(token);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
