package com.zg.tx;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2018/11/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
@Inherited
@Documented
public @interface TxTransaction {

    boolean isStart() default false;

    boolean isEnd() default false;
}
