package li.changlin.common.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target ({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
public @interface AdviceAnnotation {
}
