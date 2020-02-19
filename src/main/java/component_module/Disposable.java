package component_module;

/**
 * @author Hamza Ouni
 */
public interface Disposable {
    /**
     * Method called by the Component Manager when a singleton component is unregistered and should be destroyed.
     *
     * @throws ComponentException if an error happens during a component's destruction
     */
    void dispose() throws ComponentException;
}
