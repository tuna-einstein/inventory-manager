package com.usp.inventory.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usp.inventory.ApplicationRoot;
import com.usp.inventory.FirebaseRefs;
import com.usp.inventory.R;
import com.usp.inventory.SharedPreferencesStore;
import com.usp.inventory.dagger2.ActivityComponent;
import com.usp.inventory.dagger2.ActivityModule;
import com.usp.inventory.dagger2.ComponentReflectionInjector;
import com.usp.inventory.dagger2.DaggerActivityComponent;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by umasankar on 11/15/15.
 */
public abstract class BaseFragment extends Fragment {

    // @Inject protected SharedPreferencesStore sharedPreferencesStore;
    private ActivityComponent activityComponent;
    private ComponentReflectionInjector injector;

    @Inject
    protected SharedPreferencesStore sharedPreferencesStore;
    @Inject
    FirebaseRefs firebaseRefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(ApplicationRoot.getComponent(getActivity()))
                .activityModule(new ActivityModule(getActivity()))
                .build();
        injector = new ComponentReflectionInjector(ActivityComponent.class, activityComponent);
        injector.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getContentView(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getContentView();
}
