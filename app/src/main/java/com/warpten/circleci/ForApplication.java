package com.warpten.circleci;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by bhaskar on 15-04-12.
 */
@Qualifier @Retention(RetentionPolicy.RUNTIME)
public @interface ForApplication {
}
