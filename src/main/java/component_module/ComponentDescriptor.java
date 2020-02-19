package component_module;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Represent a component.
 *
 * @author Hamza Ouni
 */
public interface ComponentDescriptor<T> {
    /**
     * @return the class of the component role
     */
    Type getRoleType();

    /**
     * @return the hint of the component role
     */
    String getRoleHint();

    /**
     * @return the class of the component implementation
     */
    Class<? extends T> getImplementation();

    /**
     * @return the way the component should be instantiated
     */
    ComponentInstantiationStrategy getInstantiationStrategy();

    /**
     * @return the components on which this component depends
     */
    Collection<ComponentDependency<?>> getComponentDependencies();


}
