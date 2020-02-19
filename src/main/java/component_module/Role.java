package component_module;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Hamza Ouni
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Role {
}
