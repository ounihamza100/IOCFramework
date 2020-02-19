package component_module;

/**
 * @author Hamza Ouni
 */
public interface Initializable {
    /**
     * Method called by the Component Manager when the component is created for the first time.
     *
     * @throws ComponentException if an error happens during a component's initialization
     */
    void initialize() throws ComponentException;
}
