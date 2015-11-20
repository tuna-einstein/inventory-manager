package com.usp.inventory.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.usp.inventory.ApplicationRoot;
import com.usp.inventory.Constants;
import com.usp.inventory.FirebaseRefs;
import com.usp.inventory.R;
import com.usp.inventory.Rx;
import com.usp.inventory.SharedPreferencesStore;
import com.usp.inventory.dagger2.ActivityComponent;
import com.usp.inventory.dagger2.ActivityModule;
import com.usp.inventory.dagger2.ComponentReflectionInjector;
import com.usp.inventory.dagger2.DaggerActivityComponent;
import com.usp.inventory.fragment.OnFragmentInteractionListener;
import com.usp.inventory.model.User;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by umasankar on 11/14/15.
 */
public abstract class BaseActivity extends AppCompatActivity
        implements OnFragmentInteractionListener {

    private static final String GOOGLE_ACCOUNT_TYPE = "com.google";

    // @Inject protected SharedPreferencesStore sharedPreferencesStore;
    private ActivityComponent activityComponent;
    private ComponentReflectionInjector injector;

    @Inject protected SharedPreferencesStore sharedPreferencesStore;
    @Inject FirebaseRefs firebaseRefs;
    @Inject protected Rx rx;

    @Nullable
    @Bind(R.id.toolbar) Toolbar toolbar;


    protected AccountManager accountManager;
    protected Account account;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(ApplicationRoot.getComponent(this))
                .activityModule(new ActivityModule(this))
                .build();
        injector = new ComponentReflectionInjector(ActivityComponent.class, activityComponent);
        injector.inject(this);

        setContentView(getContentView());
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        accountManager = AccountManager.get(getApplicationContext());
        account = getAccountByName(sharedPreferencesStore.getAccountName());

        if (account == null) {
            pickUserAccount(getString(R.string.please_pick_account));
        } else {
            prepFirebase();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // The first time the account tries to contact the beacon service we'll pop a dialog
        // asking the user to authorize our activity. Ensure that's handled cleanly here, rather
        // than when the scan tries to fetch the status of every beacon within range.
        if (requestCode == Constants.REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                sharedPreferencesStore.setAccountName(accountName);
                account = getAccountByName(accountName);
                if (account == null) {
                    pickUserAccount(getString(R.string.please_pick_account));
                } else {
                    getToken();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The account picker dialog closed without selecting an account.
                // Notify users that they must pick an account to proceed.
                pickUserAccount(getString(R.string.please_pick_account));
            }
        } else if (requestCode == Constants.REQUEST_USER_PERMISSION_GRANTED) {
            prepFirebase();
        }
    }

    private void pickUserAccount(String descriptionOverrideText) {
        String[] allowableAccountTypes = new String[]{GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(
                null, null, allowableAccountTypes, false, descriptionOverrideText, null, null, null);
        startActivityForResult(intent, Constants.REQUEST_CODE_PICK_ACCOUNT);
    }

    private void prepFirebase() {
        if (firebaseRefs.getFirebaseRootRef().getAuth() == null
                || firebaseRefs.getFirebaseRootRef().getAuth().getExpires() <= System.currentTimeMillis() / 1000) {
            getToken();
        } else {
            // onFirebaseSetup();
            getToken();
        }
    }


    private void getToken() {
        rx.getAuthToken(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        // Do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UserRecoverableAuthException) {
                            UserRecoverableAuthException e1 = (UserRecoverableAuthException) e;
                            startActivityForResult(
                                    e1.getIntent(), Constants.REQUEST_USER_PERMISSION_GRANTED);
                        } else {
                            Snackbar.make(BaseActivity.this.findViewById(android.R.id.content),
                                    "Authentication failed: " + e.toString(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }
                    }

                    @Override
                    public void onNext(String token) {
                        registerFirebase(token);
                    }
                });
    }

    private void registerFirebase(String token) {
        firebaseRefs.getFirebaseRootRef().authWithOAuthToken("google", token,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        User user = new User();
                        user.setProvider(authData.getProvider());
                        user.setUid(authData.getUid());
                        if (authData.getProviderData().containsKey("displayName")) {
                            String displayName = authData.getProviderData().get("displayName").toString();
                            user.setDisplayName(displayName);
                        }
                        if (authData.getProviderData().containsKey("email")) {
                            user.setEmail(authData.getProviderData().get("email").toString());
                        }
                        firebaseRefs.getFirebaseUsersRef().child(authData.getUid()).setValue(user);
                        sharedPreferencesStore.setDisplayName(user.getDisplayName());
                        sharedPreferencesStore.setUid(user.getUid());
                        onFirebaseSetup();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.e("Hello", firebaseError.toString());
                        // there was an error
                    }
                });
    }

    protected void onFirebaseSetup() {

    }

    private Account getAccountByName(String accountName) {
        Account[] accounts = accountManager.getAccounts();
        for (Account account : accounts) {
            if (account.name.equals(accountName)) {
                return account;
            }
        }
        return null;
    }

    protected abstract int getContentView();

    public void replaceFragment(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public void onFragmentInteraction(Bundle data) {

    }
}
