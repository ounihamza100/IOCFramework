package context;

import java.util.List;

/**
 * @author Hamza Ouni
 */
public class DefaultScanner implements Scanner {

    private final ClassLoader classLoader;

    public DefaultScanner() {
        this(DefaultScanner.class.getClassLoader());
    }

    public DefaultScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }



    IOContext buildContext(String pkg) {
          if(pkg == null) {
              return new DefaultIOContext();
          }
          else {
              //List<Class> classes = doScan(pkj);
              return null;
          }

    }


    public IOContext run(String pkg) {
        return buildContext(pkg);
    }

    protected List<Class> doScan(String annotation, String pkg) {
        if(annotation == null) {
            getInstances();
        }
        else {

        }

    }

    protected List<Class> getInstances() {

    }

}
