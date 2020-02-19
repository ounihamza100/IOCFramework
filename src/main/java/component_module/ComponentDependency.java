package component_module;
import java.lang.reflect.Type;
/**
 * Represents a component dependency.
 *
 * @author Hamza Ouni
 */
public interface ComponentDependency<T> {

    /**
     * @return the name of the injection point (name of the field or name of the method)
     */
    String getName();

    /**
     * @return the class of the component role
     */
    Type getRoleType();

    /**
     * @return the hint of the component role
     */
    String getRoleHint();
}
