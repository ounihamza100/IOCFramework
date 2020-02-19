package component_module;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author Hamza Ouni
 */
public class DefaultComponentDependency<T> implements ComponentDependency<T> {
    private String name;
    private Type roleType;
    private String roleHint = "default";

    public DefaultComponentDependency() {

    }

    public DefaultComponentDependency(ComponentDependency<T> dependency) {

        setName(dependency.getName());
        setRoleType(dependency.getRoleType());
        setRoleHint(dependency.getRoleHint());
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Type getRoleType() {
        return this.roleType;
    }

    public void setRoleType(Type roleType) {
        this.roleType = roleType;
    }

    @Override
    public String getRoleHint() {
        return this.roleHint;
    }

    public void setRoleHint(String roleHint) {
        this.roleHint = roleHint;
    }

    private boolean equals(ComponentDependency dependency) {
        return super.equals(dependency) && Objects.equals(getName(), dependency.getName());
    }

    @Override
    public int hashCode() {

        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(getRoleType());
        builder.append(getName());

        return builder.toHashCode();
    }

}
