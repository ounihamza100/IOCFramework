package component_module;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A class to access and modify components repository.
 *
 * @author Hamza Ouni
 *
 */
public interface ComponentManager {

    /**
     * @param role the class that the component implements
     * @return true if the component is registered or false otherwise
     */
    boolean hasComponent(Type role);

    /**
     * @param role the class that the component implements
     * @param hint the hint that differentiates a component implementation from another one (the "default" hint being the default)
     * @return true if the component is registered for the passed hint or false otherwise
     */
    boolean hasComponent(Type role, String hint);

    /**
     * Find a component instance that implements that passed type.
     * If the component has a singleton annotation then this method always return the same instance.
     *
     * @param <T> the component role type
     * @param roleType the class that the component implements
     * @return the component instance
     * @throws Exception in case the component cannot be found
     */
    <T> T getInstance(Type roleType) throws ComponentException;

    /**
     * Find a component instance that implements that passed interface class.
     * If the component has a singleton annotation then this method always return the same instance.
     *
     * @param <T> the component role type
     * @param roleType the class that the component implements
     * @param roleHint the hint that differentiates a component implementation from another one (the "default" hint being the default)
     * @return the component instance
     * @throws Exception in case the component cannot be found
     */
    <T> T getInstance(Type roleType, String roleHint) throws ComponentException;

    /**
     * Find component instance for all the components implementing the provided role.
     *
     * @param role the type of the components role
     * @return the components
     * @param <T> the type of the components role
     * @throws ComponentLookupException if any error happen during component search
     */
    <T> List<T> getInstanceList(Type role) throws ComponentException;

    /**
     * Release the provided singleton instance but don't unregister the component descriptor.
     *
     * @param componentInstance the component to release passed as a component instance.
     * @throws Exception if the component's release raises an error
     */
    void releaseInstance(Object componentInstance) throws ComponentException;

    /**
     * Add a component in the component repository.
     * If a component with the same roleType and roleHint already exists it will be replaced by the new one.
     *
     * @param <T> the component role type
     * @param componentDescriptor the descriptor of the component to register.
     * @throws ComponentException error when registering component descriptor.
     */
    <T> void registerComponent(ComponentDescriptor<T> componentDescriptor) throws ComponentException;

    /**
     * Add a component in the component repository dynamically.
     * If a component with the same roleType and roleHint already exists it will be replaced by the new one.
     * This method also makes possible to set the instance instead of letting it created it from descriptor.
     *
     * @param <T> the component role type
     * @param componentDescriptor the descriptor of the component to register.
     * @param componentInstance the initial component instance
     * @throws ComponentException error when registering component descriptor.
     */
    <T> void registerComponent(ComponentDescriptor<T> componentDescriptor, T componentInstance) throws ComponentException;

    /**
     * Remove a component from the component repository.
     *
     * @param role the role identifying the component
     * @param hint the hint identifying the component
     */
    void unregisterComponent(Type role, String hint);

    /**
     * Remove a component from the component repository.
     *
     * @param componentDescriptor the component descriptor
     */
    void unregisterComponent(ComponentDescriptor<?> componentDescriptor);

    /**
     * @param <T> the component role type
     * @param role the role identifying the component
     * @param hint the hint identifying the component
     * @return the descriptor for the component matching the passed parameter or null if this component doesn't exist
     */
    <T> ComponentDescriptor<T> getComponentDescriptor(Type role, String hint);

    /**
     * @param <T> the role class for which to return all component implementations
     * @param role the role class for which to return all component implementations
     * @return all component implementations for the passed role
     */
    <T> List<ComponentDescriptor<T>> getComponentDescriptorList(Type role);








}
