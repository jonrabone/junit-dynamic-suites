package jonrabone.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines and names a test suite. Add this to an empty class, and use {@link org.junit.runner.RunWith}
 * with the {@link jonrabone.junit.RuntimeSuite} test runner to create a test suite.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SuiteName {
    String value();
}
