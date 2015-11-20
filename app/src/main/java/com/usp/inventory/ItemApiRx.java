package com.usp.inventory;

import com.usp.inventory.backend.itemApi.ItemApi;
import com.usp.inventory.backend.itemApi.model.Item;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by umasankar on 11/11/15.
 */
public class ItemApiRx {

    private ItemApi itemApi;

    public ItemApiRx(ItemApi itemApi) {
        this.itemApi = itemApi;
    }

    public Observable<Item> addItem(final Item itemToAdd) {

        return Observable.create(new Observable.OnSubscribe<Item>() {
            @Override
            public void call(Subscriber<? super Item> subscriber) {
                try {
                    Item item = itemApi.insert(itemToAdd).execute();
                    subscriber.onNext(item);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
