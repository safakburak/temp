package com.ekinoksyazilim.etkk.prototype.configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface config {

    String value();
}
