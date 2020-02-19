package component_module;

/**
 * @author Hamza Ouni
 */
public enum ComponentInstantiationStrategy {

    /**
     * The same component implementation instance is returned for all lookups.
     */
    SINGLETON,

    /**
     * A new component implementation instance is created at a each lookup.
     */
    PER_LOOKUP
}
