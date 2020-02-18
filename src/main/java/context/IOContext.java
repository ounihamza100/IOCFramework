package context;

/**
 * @author Hamza Ouni
 */
public interface IOContext {

    /**
     *
     * @param name
     * @return get Object from the context has the given name
     */
    Object getObject(String name) /** throw exception beanNotfound */;

    /**
     *
     * @param objectType
     * @return get Object from the context has the given type
     */
    Object getObject(Class objectType)/** throw exception beanNotfound */;
}
