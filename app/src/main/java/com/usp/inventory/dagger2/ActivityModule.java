package com.usp.inventory.dagger2;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;

import dagger.Module;
import dagger.Provides;

/**
 * Created by umasankar on 10/3/15.
 */
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    Activity provideActivity() {
        return activity;
    }

    @ActivityScope
    @Provides
    ProgressDialog getDialog(Activity activity) {
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please wait...");
        return dialog;
    }
}