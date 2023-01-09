package be.better.spring.core.web.servlet.mvc;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
// 用于类上
@Target(ElementType.TYPE)
public @interface Controller {
}
