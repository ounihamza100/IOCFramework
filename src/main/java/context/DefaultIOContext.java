package context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hamza Ouni
 */
public class DefaultIOContext implements IOContext {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultIOContext.class);

    private Scanner scanner;

    public DefaultIOContext() {
    }

    public DefaultIOContext(Scanner scanner) throws Exception {
        if (scanner == null) throw new Exception();//Should return specific message
        this.scanner = scanner;
    }


    @Override
    public Object getObject(String name) {
        return null;
    }

    @Override
    public Object getObject(Class objectType) {
        return null;
    }
}
