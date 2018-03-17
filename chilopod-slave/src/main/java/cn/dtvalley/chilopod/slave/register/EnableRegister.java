package cn.dtvalley.chilopod.slave.register;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RegisterConfiguration.class)
public @interface EnableRegister {
}
