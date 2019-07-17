package com.edu.cjp.beans;

import java.lang.annotation.*;

/**
 * Created by 澎仔 on 2019/6/28.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {
}
