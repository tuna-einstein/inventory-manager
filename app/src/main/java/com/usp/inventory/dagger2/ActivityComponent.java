package com.usp.inventory.dagger2;

import android.app.Activity;
import android.app.ProgressDialog;

import com.usp.inventory.activity.BaseActivity;
import com.usp.inventory.activity.InventoryActivity;
import com.usp.inventory.activity.MainActivity;
import com.usp.inventory.activity.AddItemActivity;
import com.usp.inventory.activity.PlaceOrderActivity;
import com.usp.inventory.fragment.ListRequestFragment;
import com.usp.inventory.fragment.ListItemFragment;
import com.usp.inventory.fragment.MarketFragment;

import dagger.Component;

/**
 * Created by umasankar on 10/3/15.
 */
@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {
                ActivityModule.class,
        }
)
public interface ActivityComponent {

    void inject(BaseActivity activity);

    void inject(MainActivity activity);

    void inject(AddItemActivity activity);

    void inject(InventoryActivity activity);

    void inject(PlaceOrderActivity activity);

    void inject(ListItemFragment fragment);

    void inject(MarketFragment fragment);

    void inject(ListRequestFragment fragment);

    Activity activity();

    ProgressDialog progressDialog();

}
