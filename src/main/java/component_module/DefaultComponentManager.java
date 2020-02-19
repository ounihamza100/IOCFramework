package component_module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of ComponentManager.
 *
 * @author Hamza Ouni
 */
@Component
@Singleton
public class DefaultComponentManager implements ComponentManager {

    private static final Logger logger = LoggerFactory.getLogger(DefaultComponentManager.class);

    public DefaultComponentManager() {

    }

    private static class ComponentEntry<R> {
        // Descriptor of the component.
        public final ComponentDescriptor<R> descriptor;
        /**
         * Cached instance of the component. Lazily initialized when needed.
         * Volatile to ensure it's really shared and sync between all threads.
         */
        public volatile R instance;

        public ComponentEntry(ComponentDescriptor<R> descriptor, R instance) {
            this.descriptor = descriptor;
            this.instance = instance;
        }


    }

    private Map<Type, Map<String, ComponentEntry<?>>> componentEntries = new ConcurrentHashMap<>();

    private ComponentEntry<?> getComponentEntry(Type role, String hint) {
        Map<String, ComponentEntry<?>> entries = this.componentEntries.get(role);
        if (entries != null) {
            return entries.get(hint != null ? hint : "default");
        }

        return null;
    }

    @Override
    public boolean hasComponent(Type role) {
        return hasComponent(role, null);
    }

    @Override
    public boolean hasComponent(Type role, String hint) {
        if (getComponentEntry(role, hint) != null) {
            return true;
        }
        return false;
    }

    @Override
    public <T> T getInstance(Type roleType) throws ComponentException {
        return getInstance(roleType, null);
    }

    @Override
    public <T> T getInstance(Type roleType, String roleHint) throws ComponentException {
        T instance;
        ComponentEntry<T> componentEntry = (ComponentEntry<T>) getComponentEntry(roleType, roleHint);
        if (componentEntry != null) {
            try {
                instance = getComponentInstance(componentEntry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    private <T> T getComponentInstance(ComponentEntry<T> componentEntry) throws Exception {
        T instance;
        ComponentDescriptor<T> descriptor = componentEntry.descriptor;
        if (descriptor.getInstantiationStrategy() == ComponentInstantiationStrategy.SINGLETON) {
            if (componentEntry.instance != null) {
                // If the instance exists return it
                instance = componentEntry.instance;
            } else {
                synchronized (componentEntry) {
                    // Re-check in case
                    if (componentEntry.instance != null) {
                        instance = componentEntry.instance;
                    } else {
                        componentEntry.instance = createInstance(descriptor);
                        instance = componentEntry.instance;
                    }
                }
            }
        } else {
            instance = createInstance(descriptor);
        }
        return instance;
    }

    /**
     * Create component instance from a ComponentDescriptor
     *
     * @param descriptor
     * @return instance
     * @throws Exception
     */
    private <T> T createInstance(ComponentDescriptor<T> descriptor) throws Exception {

        // Create instance from class implementation
        T instance = descriptor.getImplementation().newInstance();

        // Set each dependency field value
        for (ComponentDependency<?> dependency : descriptor.getComponentDependencies()) {
            // Handle different field types
            Object fieldValue = getDependencyInstance(descriptor, instance, dependency);

            // Set the field by introspection
            if (fieldValue != null) {
                ReflectionUtils.setFieldValue(instance, dependency.getName(), fieldValue);
            }
        }

        return instance;
    }

    protected Object getDependencyInstance(ComponentDescriptor<?> descriptor, Object parentInstance,
                                           ComponentDependency<?> dependency) throws ComponentException {

        // Handle different field types
        Object fieldValue;

        Class<?> dependencyRoleClass = ReflectionUtils.getTypeClass(dependency.getRoleType());
        fieldValue = getInstance(dependency.getRoleType(), dependency.getRoleHint());
        return fieldValue;
    }

    private void removeComponent(Type role, String hint) throws ComponentException {
        Map<String, ComponentEntry<?>> entries = this.componentEntries.get(role);
        if (entries != null) {
            ComponentEntry<?> componentEntry = entries.remove(hint != null ? hint : "default");

            if (componentEntry != null) {
                ComponentDescriptor<?> oldDescriptor = componentEntry.descriptor;

                // We don't want the component manager to dispose itself
                if (componentEntry.instance != this) {
                    // clean any resource associated to the component instance and descriptor
                    releaseComponentEntry(componentEntry);
                }
            }
        }

    }

    @Override
    public <T> List<T> getInstanceList(Type role) throws ComponentException {
        return null;
    }

    @Override
    public void releaseInstance(Object componentInstance) throws ComponentException {
        // First find the descriptor matching the passed component
        ComponentEntry<?> componentEntry = null;
        for (Map<String, ComponentEntry<?>> entries : this.componentEntries.values()) {
            for (ComponentEntry<?> entry : entries.values()) {
                if (entry.instance == componentInstance) {
                    componentEntry = entry;

                    break;
                }
            }
        }

        if (componentEntry != null) {
            // Release the entry
            releaseInstance(componentEntry);
        }
    }

    private void releaseInstance(ComponentEntry<?> componentEntry) throws ComponentException {

        synchronized (componentEntry) {
            Object instance = componentEntry.instance;

            // TODO: component clean up
            componentEntry.instance = null;
        }
    }

    private void releaseComponentEntry(ComponentEntry<?> componentEntry) throws ComponentException {
        // clean existing instance
        releaseInstance(componentEntry);
    }

    @Override
    public <T> void registerComponent(ComponentDescriptor<T> componentDescriptor) throws ComponentException {
        registerComponent(componentDescriptor, null);
    }

    @Override
    public <T> void registerComponent(ComponentDescriptor<T> componentDescriptor, T componentInstance) {
        // Remove any existing component associated to the provided roleHint
        try {
            removeComponent(componentDescriptor.getRoleType(), componentDescriptor.getRoleHint());
        } catch (Exception e) {
            this.logger.warn("Some exception raised during removing component.", e);
        }

        logger.debug("[DefautComponentManager] registerComponent roletype=" + componentDescriptor.getRoleType()
                + " rolehint=" + componentDescriptor.getRoleHint());
        // Register new component
        addComponent(new DefaultComponentDescriptor<T>(componentDescriptor), componentInstance);
    }

    private <T> void addComponent(ComponentDescriptor<T> descriptor, T instance) {

        ComponentEntry<T> componentEntry = new ComponentEntry<>(descriptor, instance);

        // Register new component
        Map<String, ComponentEntry<?>> entries = this.componentEntries.get(descriptor.getRoleType());
        if (entries == null) {
            entries = new ConcurrentHashMap<>();
            this.componentEntries.put(descriptor.getRoleType(), entries);

            logger.debug("[DefaultComponentManager] addComponent type=" + descriptor.getRoleType() + " hint=" + descriptor.getRoleHint());
        }
        entries.put(descriptor.getRoleHint(), componentEntry);
    }

    @Override
    public void unregisterComponent(Type role, String hint) {
        try {
            removeComponent(role, hint);
        } catch (Exception e) {
            this.logger.warn("Some exception raised during removing component.", e);
        }
    }

    @Override
    public void unregisterComponent(ComponentDescriptor<?> componentDescriptor) {
        if (Objects.equals(getComponentDescriptor(componentDescriptor.getRoleType(), componentDescriptor.getRoleHint()), componentDescriptor)) {
            unregisterComponent(componentDescriptor.getRoleType(), componentDescriptor.getRoleHint());
        }
    }

    @Override
    public <T> ComponentDescriptor<T> getComponentDescriptor(Type role, String hint) {
        ComponentDescriptor<T> result = null;
        ComponentEntry<?> componentEntry = getComponentEntry(role, hint);
        if (componentEntry != null) {
            result = (ComponentDescriptor<T>) componentEntry.descriptor;
        }
        return result;
    }

    @Override
    public <T> List<ComponentDescriptor<T>> getComponentDescriptorList(Type role) {
        Map<String, ComponentDescriptor<T>> descriptors = new HashMap<>();

        // Add local descriptors
        Map<String, ComponentEntry<?>> enries = this.componentEntries.get(role);
        if (enries != null) {
            for (Map.Entry<String, ComponentEntry<?>> entry : enries.entrySet()) {
                descriptors.put(entry.getKey(), (ComponentDescriptor<T>) entry.getValue().descriptor);
            }
        }

        return new ArrayList<>(descriptors.values());
    }


}