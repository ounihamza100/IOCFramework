import context.DefaultIOContext;
import context.IOContext;

/**
 * @author Hamza Ouni
 */
public class DefaultIOContextTest {

    IOContext defaultIOContext = new DefaultIOContext("package : context.DefaultIOContext");
    A a = defaultIOContext.getObject(A.class);


    static class A {

    }
}
