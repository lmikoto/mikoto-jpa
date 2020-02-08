package io.github.lmikoto.jpa;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({JpaConfig.class})
@Documented
public @interface EnableMikotoJpa {
}
