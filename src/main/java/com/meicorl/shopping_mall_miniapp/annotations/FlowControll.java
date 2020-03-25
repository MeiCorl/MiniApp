package com.meicorl.shopping_mall_miniapp.annotations;

import java.lang.annotation.*;

/**
 * 接口限流注解
 * @author caomei
 * @date 2019/9/16
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface FlowControll {
    /* 限流动作 */
    String action() default "";

    /* 限流时间间隔 */
    int interval() default 1;
}
