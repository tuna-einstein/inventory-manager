package com.usp.inventory.dagger2;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.usp.inventory.FirebaseRefs;
import com.usp.inventory.ItemApiRx;
import com.usp.inventory.SharedPreferencesStore;
import com.usp.inventory.backend.itemApi.ItemApi;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;

/**
 * Created by umasankar on 10/3/15.
 */
@Module
public class ApplicationModule {

    private final Application application;

    private static final String ACCOUNT_KEY = "accountName";

    private static final String FIREBASE_URL =
            "https://burning-torch-8564.firebaseio.com/inventory-management";

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @AppScope
    @Provides
    Application application() {
        return application;
    }

    @AppScope
    @Provides
    public ItemApi provideItemApi(
            HttpTransport httpTransport,
            JacksonFactory jacksonFactory,
            HttpRequestInitializer httpRequestInitializer) {
        return new ItemApi.Builder(httpTransport, jacksonFactory, httpRequestInitializer)
                .setApplicationName("usp")
                .build();
    }

    @AppScope
    @Provides
    public ItemApiRx provideItemApiRx(ItemApi itemApi) {
        return new ItemApiRx(itemApi);
    }

    @AppScope @Provides
    HttpTransport getHttpTransport() {
        return new NetHttpTransport();
    }

    @AppScope @Provides
    JacksonFactory getJacksonFactory() {
        return new JacksonFactory();
    }

    @AppScope @Provides
    HttpRequestInitializer provideRequestInitializer() {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {

            }
        };
    }

    @AppScope @Provides
    public FirebaseRefs getFirebaseRef() {
        return new FirebaseRefs(new Firebase(FIREBASE_URL));
    }

    @Provides
    @AppScope
    SharedPreferencesStore getSharedPrefs(Application application) {
        return new SharedPreferencesStore(PreferenceManager.getDefaultSharedPreferences(application));
    }

    @Provides
    @AppScope
    Gson getGson() {
        return new Gson();
    }
}