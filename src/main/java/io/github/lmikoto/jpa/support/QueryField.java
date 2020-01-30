package io.github.lmikoto.jpa.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface QueryField {

    /**
     * 搜索类型
     */
    QueryType type();

    /**
     * 搜索的字段名，默认为搜索对象的属性名
     */
    String name() default "";
}
