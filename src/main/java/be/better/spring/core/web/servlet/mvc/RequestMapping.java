package be.better.spring.core.web.servlet.mvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
// 用于方法上
@Target(ElementType.METHOD)
public @interface RequestMapping {
    /**
     * 记录uri
      */
    String value();
}
