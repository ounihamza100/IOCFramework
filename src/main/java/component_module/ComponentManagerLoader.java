package component_module;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hamza Ouni
 */
public class ComponentManagerLoader {

    private ComponentDescriptorFactory factory;

    public ComponentManagerLoader() {
        factory = new ComponentDescriptorFactory();
    }

    public void initialize(DefaultComponentManager manager) {

//        registerComponent(manager, DefaultObservationManager.class);
//
//        // Environment
//        registerComponent(manager, WebEnvironment.class);
//
//        // Mail components implementation
//        registerComponent(manager, DefaultMailSenderConfiguration.class);
//        registerComponent(manager, MailSenderInitializerListener.class);
//        registerComponent(manager, SendMailRunnable.class);
//        registerComponent(manager, FileSystemMailContentStore.class);
//        registerComponent(manager, SendMailQueueManager.class);
//        registerComponent(manager, DefaultMailSender.class);
//        registerComponent(manager, DefaultSkinManager.class);
    }

    public void registerComponent(DefaultComponentManager manager, Class<?> componentClass) {

        List<ComponentDescriptor> descriptors = getComponentsDescriptors(componentClass);
        for (ComponentDescriptor componentDescriptor : descriptors) {
            try {
                manager.registerComponent(componentDescriptor);
            } catch (ComponentException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ComponentDescriptor> getComponentsDescriptors(Class<?> componentClass) {

        List<ComponentDescriptor> descriptors = new ArrayList<>();
        for (Type componentRoleType : findComponentRoleTypes(componentClass)) {
            descriptors.addAll(this.factory.createComponentDescriptors(componentClass, componentRoleType));
        }

        return descriptors;
    }

    public Set<Type> findComponentRoleTypes(Class<?> componentClass) {

        // Note: We use a Set to ensure that we don't register duplicate roles.
        Set<Type> types = new LinkedHashSet<>();

        // Returns this element's annotation for the specified type if such an annotation is present, else null.
        Component component = componentClass.getAnnotation(Component.class);

        // Auto-discover roles by looking for a @Role annotation
        for (Type interfaceType : getGenericInterfaces(componentClass)) {
            Class<?> interfaceClass;

            if (interfaceType instanceof Class) {
                interfaceClass = (Class<?>) interfaceType;
            } else {
                continue;
            }

            // Handle superclass of interfaces
            types.addAll(findComponentRoleTypes(interfaceClass));

            // Handle interfaces directly declared in the passed component class
            if (ReflectionUtils.getDirectAnnotation(Role.class, interfaceClass) != null) {
                types.add(interfaceType);
            }
        }

        // Note that we need to look into the superclass since the super class can itself implements an interface
        // that has the @Role annotation.
        Type superType = componentClass.getGenericSuperclass();
        if (superType != null && superType != Object.class) {
            if (superType instanceof Class) {
                types.addAll(findComponentRoleTypes((Class) superType));
            }
        }

        return types;
    }

    /**
     * Helper method that generate a RuntimeException in case of a reflection error.
     *
     * @param componentClass the component for which to return the interface types
     * @return the Types representing the interfaces directly implemented by the class or interface represented by this object
     * @throws RuntimeException in case of a reflection error
     */
    private Type[] getGenericInterfaces(Class<?> componentClass) {
        Type[] interfaceTypes;
        try {
            interfaceTypes = componentClass.getGenericInterfaces();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to get interface for [%s]", componentClass.getName()), e);
        }
        return interfaceTypes;
    }
}
