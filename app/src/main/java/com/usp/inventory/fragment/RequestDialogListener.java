package com.usp.inventory.fragment;

import com.usp.inventory.model.Item;
import com.usp.inventory.model.User;

/**
 * Created by umasankar on 11/17/15.
 */
public interface RequestDialogListener {

    Item getItem();

    User getItemOwner();

    User requester();

}
