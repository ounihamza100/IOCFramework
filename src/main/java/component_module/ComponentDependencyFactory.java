package component_module;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;

/**
 *
 * Allows creating ComponentDependency instance from a Field.
 * @author Hamza Ouni
 */
public class ComponentDependencyFactory {
    public ComponentDependencyFactory() {
    }

    public ComponentDependency  createComponentDependency(Field field) {
        DefaultComponentDependency dependency;
        Inject inject = field.getAnnotation(Inject.class);
        if(inject != null) {
            dependency = new DefaultComponentDependency();
            dependency.setRoleType(field.getGenericType());
            dependency.setName(field.getName());
            // Look for a Named annotation
            Named named = field.getAnnotation(Named.class);
            if (named != null) {
                dependency.setRoleHint(named.value());
            }

        }
        else {
            dependency = null;
        }

        return dependency;

    }



}
