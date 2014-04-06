package jonrabone.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks entire test classes or individual methods as belonging to a specific test suite.
 * The {@link jonrabone.junit.InSuite} annotation can be used on both methods and classes, but methods take
 * priority.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InSuite {
    String[] value();
}
