package jonrabone.junit;

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility functions for working with annotations.
 */
public final class Helpers {

    private Helpers() {
    }

    /**
     * Check if the suite name matches the list of suites passed in inSuites.
     *
     * @param inSuites  annotation to test
     * @param suiteName suite name to match
     * @return true if the suite name is found in the values of the inSuites annotation,
     * false otherwise.
     */
    public static boolean matchesSuiteName(final InSuite inSuites, final SuiteName suiteName) {
        if (inSuites != null) {
            for (String suite : inSuites.value()) {
                if (suiteName.value().equals(suite)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the class has any method which has an InSuite annotation matching the given suite name.
     *
     * @param cls       the class to check.
     * @param suiteName the suite name to match.
     * @return true if the suite name is found in the values of the inSuites annotation of any
     * method on the class, false otherwise.
     */
    public static boolean classInSuite(final Class<?> cls, final SuiteName suiteName) {
        final InSuite classInSuite = cls.getAnnotation(InSuite.class);
        for (Method method : cls.getMethods()) {
            if (methodInSuite(method, classInSuite, suiteName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the method has an InSuite annotation matching the given suite name, given any
     * annotation present on the parent class.
     *
     * @param method       the method to check.
     * @param classInSuite the InSuite annotation on the parent class, or null.
     * @param suiteName    the suite name to match.
     * @return true if the suite name is found in the values of the inSuites annotation of the
     * method and it does not conflict with any annotation on the parent class, false otherwise.
     */
    public static boolean methodInSuite(final Method method, final InSuite classInSuite, final SuiteName suiteName) {
        final boolean classPresent = (classInSuite != null);
        final boolean classMatches = matchesSuiteName(classInSuite, suiteName);
        final boolean methodPresent = method.isAnnotationPresent(InSuite.class);
        final boolean methodMatches = matchesSuiteName(method.getAnnotation(InSuite.class), suiteName);
        if (classPresent && methodPresent && (classMatches != methodMatches)) {
            // TODO consider using a logging framework (spit)
            System.err.println("ignoring mismatched suite annotation on method " + method +
                    " : entire class marked with suite(s) " + Arrays.toString(classInSuite.value()));
            return false;
        }
        return (classMatches || methodMatches);
    }

    /**
     * Find all classes on the same classpath as a supplied base class with the given annotation. Only class and method
     * annotations are scanned.
     *
     * @param baseClass       class to determine base classpath from.
     * @param annotationClass annotation to look for.
     * @return a set of class names.
     * @throws IOException if an IO error occurred.
     */
    public static Set<String> findAnnotatedClasses(final Class<?> baseClass, final Class<?> annotationClass) throws IOException {
        final AnnotationDB db = new AnnotationDB();
        db.setScanClassAnnotations(true);
        db.setScanMethodAnnotations(true);
        db.setScanFieldAnnotations(false);
        db.setScanParameterAnnotations(false);

        db.scanArchives(ClasspathUrlFinder.findClassBase(baseClass));

        return db.getAnnotationIndex().get(annotationClass.getName());
    }

    /**
     * Filter a set of classnames to only those which are marked with or contain methods marked with
     * {@link InSuite} annotations matching the given suite name.
     *
     * @param suiteName  the suite name to match.
     * @param classNames class names to scan.
     * @return a set of filtered classes, may be empty.
     * @throws ClassNotFoundException if a class cannot be loaded.
     */
    public static Set<Class<?>> filterClassesToSuite(final SuiteName suiteName, final Set<String> classNames) throws ClassNotFoundException {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String className : classNames) {
            final Class<?> cls = Class.forName(className);
            if (classInSuite(cls, suiteName)) {
                classes.add(cls);
            }
        }
        return classes;
    }
}
