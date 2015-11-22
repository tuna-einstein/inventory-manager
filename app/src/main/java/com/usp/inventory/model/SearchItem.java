package com.usp.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by umasankar on 11/20/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchItem {

    private String key;

    private Item value;

    public String getKey() {
        return key;
    }

    public Item getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Item value) {
        this.value = value;
    }
}
