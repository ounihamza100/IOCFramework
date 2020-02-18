package context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation behave like java annotation @Inject
 *
 * @author Hamza Ouni
 */
@Target({ElementType.CONSTRUCTOR, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface Push {

}
