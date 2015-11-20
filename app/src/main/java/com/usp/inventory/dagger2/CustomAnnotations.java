package com.usp.inventory.dagger2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by umasankar on 11/12/15.
 */
public class CustomAnnotations {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Item {}

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Root {}

    @Retention(RetentionPolicy.RUNTIME)
    public @interface User {}
}
