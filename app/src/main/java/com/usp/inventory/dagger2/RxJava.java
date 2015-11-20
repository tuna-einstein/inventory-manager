package com.usp.inventory.dagger2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by umasankar on 11/11/15.
 */
public class RxJava {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface AddItem {}
}
