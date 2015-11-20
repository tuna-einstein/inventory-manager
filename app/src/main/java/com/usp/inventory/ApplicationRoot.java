package com.usp.inventory;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.firebase.client.Firebase;
import com.usp.inventory.dagger2.ApplicationComponent;
import com.usp.inventory.dagger2.ApplicationModule;
import com.usp.inventory.dagger2.DaggerApplicationComponent;

/**
 * Created by umasankar on 10/3/15.
 */
public class ApplicationRoot extends MultiDexApplication {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        //Note that this method must be called before any calls to Firebase
        // and only needs to be called once per application
        Firebase.setAndroidContext(this);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }

    public static ApplicationComponent getComponent(Context context) {
        return ((ApplicationRoot) context.getApplicationContext()).getComponent();
    }
}
