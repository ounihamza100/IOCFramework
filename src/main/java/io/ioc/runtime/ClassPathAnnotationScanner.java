package io.ioc.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

/**
 * <p>An optimized classpath scanner that includes the ability to optionally scan JAR files.</p>
 * <p>The implementation avoids loading the classes themselves by parsing the class definitions and reading
 * only the annotations.</p>
 *
 * @author Hamza Ouni
 */
public class ClassPathAnnotationScanner implements AnnotationScanner {

    private static final Logger LOG = LoggerFactory.getLogger(ClassPathAnnotationScanner.class);

    private final ClassLoader classLoader;
    private boolean includeJars;


    /**
     * @param classLoader The class loader
     */
    public ClassPathAnnotationScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.includeJars = true;
    }

    /**
     * Default constructor.
     */
    public ClassPathAnnotationScanner() {
        this(ClassPathAnnotationScanner.class.getClassLoader());
    }

    /**
     * Whether to include JAR files.
     *
     * @param includeJars The jar files to include
     * @return This scanner
     */
    protected ClassPathAnnotationScanner includeJars(boolean includeJars) {
        this.includeJars = includeJars;
        return this;
    }



    @Override
    public Stream<Class> scan(String annotation, String pkg) {
        return null;
    }
}
