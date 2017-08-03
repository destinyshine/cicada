package me.destinyshine.cicada.broker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liujianyu.ljy on 17/7/31.
 *
 * @author liujianyu.ljy
 * @date 2017/07/31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BytesField {

    int order();

    int size() default 0;
}
