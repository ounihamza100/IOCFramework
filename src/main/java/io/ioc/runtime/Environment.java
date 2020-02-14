package io.ioc.runtime;

import java.util.Collection;
import java.util.Set;

/**
 * The current application environment.
 *
 * @author Hamza Ouni
 */
public interface Environment {
    /**
     * The test environment.
     */
    String TEST = "test";

    /**
     * The development environment.
     */
    String DEVELOPMENT = "dev";

    /**
     * The android environment.
     */
    String ANDROID = "android";

    /**
     * The cloud environment.
     */
    String CLOUD = "cloud";

    /**
     * The default application name.
     */
    String DEFAULT_NAME = "application";



    /**
     * @return The active environment names
     */
    Set<String> getActiveNames();

    /**
     * @return The application packages
     */
    Collection<String> getPackages();






}
