package com.usp.inventory.dagger2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Application level scope.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScope {
}