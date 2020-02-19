package component_module;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of ComponentDescriptor interface.
 *
 * @author Hamza Ouni
 */
public class DefaultComponentDescriptor<T> implements ComponentDescriptor<T> {

    private Class<? extends T> implementation;
    private ComponentInstantiationStrategy instantiationStrategy = ComponentInstantiationStrategy.SINGLETON;
    private List<ComponentDependency<?>> componentDependencies = new ArrayList<ComponentDependency<?>>();

    private Type roleType;
    private String roleHint;


    public DefaultComponentDescriptor() {

    }

    /**
     * Creating a new DefaultComponentDescriptor by cloning.
     *
     * @param descriptor the component descriptor to clone
     */
    public DefaultComponentDescriptor(ComponentDescriptor<T> descriptor) {

        setImplementation(descriptor.getImplementation());
        setInstantiationStrategy(descriptor.getInstantiationStrategy());
        setRoleType(descriptor.getRoleType());
        setRoleHint(descriptor.getRoleHint());

        for (ComponentDependency<?> dependency : descriptor.getComponentDependencies()) {
            addComponentDependency(new DefaultComponentDependency(dependency));
        }
    }

    @Override
    public Class<? extends T> getImplementation() {
        return this.implementation;
    }

    public void setImplementation(Class<? extends T> implementation) {
        this.implementation = implementation;
    }

    @Override
    public ComponentInstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

    public void setInstantiationStrategy(ComponentInstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    @Override
    public Collection<ComponentDependency<?>> getComponentDependencies() {
        return this.componentDependencies;
    }

    public void addComponentDependency(ComponentDependency<?> componentDependency) {
        this.componentDependencies.add(componentDependency);
    }

    @Override
    public Type getRoleType() {
        return roleType;
    }

    public void setRoleType(Type roleType) {
        this.roleType = roleType;
    }

    @Override
    public String getRoleHint() {
        return roleHint;
    }

    public void setRoleHint(String roleHint) {
        this.roleHint = roleHint;
    }

    @Override
    public boolean equals(Object object) {

        boolean result;

        // See http://www.technofundo.com/tech/java/equalhash.html for the detail of this algorithm.
        if (this == object) {
            result = true;
        } else {
            if ((object == null) || (object.getClass() != this.getClass())) {
                result = false;
            } else {
                // object must be Syntax at this point
                ComponentDescriptor cd = (ComponentDescriptor) object;
                result =
                        super.equals(cd) && Objects.equals(getImplementation(), cd.getImplementation())
                                && Objects.equals(getInstantiationStrategy(), cd.getInstantiationStrategy())
                                && Objects.equals(getComponentDependencies(), cd.getComponentDependencies());
            }
        }

        return result;
    }

    @Override
    public int hashCode() {

        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(getImplementation());
        builder.append(getInstantiationStrategy());
        builder.append(getComponentDependencies());

        return builder.toHashCode();
    }
}
