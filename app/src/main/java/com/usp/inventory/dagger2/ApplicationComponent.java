package com.usp.inventory.dagger2;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.usp.inventory.FirebaseRefs;
import com.usp.inventory.ItemApiRx;
import com.usp.inventory.SharedPreferencesStore;
import com.usp.inventory.backend.itemApi.ItemApi;

import dagger.Component;

/**
 * Contains bindings at application level.
 */
@AppScope
@Component(
        modules = {
                ApplicationModule.class,
        }
)
public interface ApplicationComponent {

    Application application();

    ItemApiRx itemApiRx();

    FirebaseRefs firebase();

    SharedPreferencesStore sharedPreferencesStore();

    Gson gson();
}
