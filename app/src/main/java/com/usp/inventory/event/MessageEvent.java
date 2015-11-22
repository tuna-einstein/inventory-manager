package com.usp.inventory.event;

/**
 * Created by umasankar on 11/21/15.
 */
public class MessageEvent {
    public final EventType eventType;

    public MessageEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public enum EventType {
        MY_ORDERS_CHANGED,
        MY_APPROVALS_CHANGED,
        MY_ITEMS_CHANGED
    }
}
