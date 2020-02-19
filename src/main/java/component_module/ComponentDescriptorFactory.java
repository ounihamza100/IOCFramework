package component_module;

import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows creating ComponentDescriptor instances from a Component.
 *
 * @author Hamza Ouni
 */
public class ComponentDescriptorFactory {

    /**
     * Create component descriptors for the passed component implementation class and component role class.
     *
     * @param componentClass the component implementation class
     * @param componentRoleType the component role type
     * @return the component descriptors with resolved component dependencies
     */
    public List<ComponentDescriptor> createComponentDescriptors(Class<?> componentClass, Type componentRoleType) {

        List<ComponentDescriptor> descriptors = new ArrayList<ComponentDescriptor>();

        // If there's a @Named annotation, use it.
        String[] hints;
        Named named = componentClass.getAnnotation(Named.class);
        if (named != null) {
            hints = new String[] {named.value()};
        } else {
            hints = new String[] {"default"};
        }

        // Create the descriptors
        for (String hint : hints) {
            descriptors.add(createComponentDescriptor(componentClass, hint, componentRoleType));
        }

        return descriptors;
    }

    /**
     * Create a component descriptor for the passed component implementation class, hint and component role class.
     *
     * @param componentClass the component implementation class
     * @param hint the hint
     * @param componentRoleType the component role type
     * @return the component descriptor with resolved component dependencies
     */
    private ComponentDescriptor createComponentDescriptor(Class<?> componentClass, String hint, Type componentRoleType) {

        DefaultComponentDescriptor descriptor = new DefaultComponentDescriptor();
        descriptor.setRoleType(componentRoleType);
        descriptor.setRoleHint(hint);
        descriptor.setImplementation(componentClass);
        descriptor.setInstantiationStrategy(createComponentInstantiationStrategy(componentClass));

        // Set the injected fields.
        for (Field field : ReflectionUtils.getAllFields(componentClass)) {
            ComponentDependency dependency = createComponentDependency(field);
            if (dependency != null) {
                descriptor.addComponentDependency(dependency);
            }
        }

        return descriptor;
    }

    /**
     * @param componentClass the component class from which to extract the component instantiation strategy
     * @return the component instantiation strategy to use
     */
    private ComponentInstantiationStrategy createComponentInstantiationStrategy(Class<?> componentClass) {

        ComponentInstantiationStrategy strategy;

        // Support both InstantiationStrategy and JSR 330's Singleton annotations.
        Singleton singleton = componentClass.getAnnotation(Singleton.class);
        if (singleton != null) {
            strategy = ComponentInstantiationStrategy.SINGLETON;
        } else {
            // TODO: verify annotation in parents implementation
            strategy = ComponentInstantiationStrategy.PER_LOOKUP;
        }

        return strategy;
    }

    /**
     * @param field the field for which to extract a Component Dependency
     * @return the Component Dependency instance created from the passed field
     */
    private ComponentDependency createComponentDependency(Field field) {

        ComponentDependency dependency = new ComponentDependencyFactory().createComponentDependency(field);
        return dependency;
    }
}
