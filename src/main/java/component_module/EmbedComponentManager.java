package component_module;

import javax.inject.Singleton;
import java.util.List;

/**
 * @author Hamza Ouni
 */
@Singleton
public class EmbedComponentManager extends DefaultComponentManager implements Initializable, Disposable {

    public EmbedComponentManager() {
        super();

        registerThis();
    }

    private void registerThis() {

        ComponentDescriptorFactory factory = new ComponentDescriptorFactory();
        List<ComponentDescriptor> descriptors = factory.createComponentDescriptors(this.getClass(), ComponentManager.class);

//        DefaultComponentDescriptor<ComponentManager> cd = new DefaultComponentDescriptor<>();
//        cd.setRoleType(ComponentManager.class);

        registerComponent(descriptors.get(0), this);
    }

    @Override
    public void initialize() throws ComponentException {
    }

    @Override
    public void dispose() throws ComponentException {
    }

    public void initializeComponents() {

        // Load all component annotations and register them as components
        ComponentManagerLoader loader = new ComponentManagerLoader();
        loader.initialize(this);
    }
}

