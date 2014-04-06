package jonrabone.junit;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A JUnit test runner which supports finding suites of test classes at runtime using annotations
 * instead of hardcoded configuration or XML files. Test suites are defined as an empty class
 * with an {@link org.junit.runner.RunWith} annotation specifying this runner, and a
 * {@link jonrabone.junit.SuiteName} annotation to specify the name of the suite.
 * The scannotation library is used to do the actual classpath scanning.
 */
public class RuntimeSuite extends ParentRunner<Runner> {

    private final List<Runner> runners;

    public RuntimeSuite(final Class<?> testClass) throws InitializationError {
        super(testClass);
        final SuiteName suiteName = testClass.getAnnotation(SuiteName.class);
        if (suiteName == null) {
            throw new InitializationError("missing @SuiteName annotation on suite class '" + testClass + "'");
        }
        try {
            final Set<String> classNames = Helpers.findAnnotatedClasses(testClass, InSuite.class);
            if (classNames == null || classNames.isEmpty()) {
                throw new InitializationError("no test classes on classpath with @InSuite annotation");
            }
            final Set<Class<?>> classes = Helpers.filterClassesToSuite(suiteName, classNames);
            if (classes.isEmpty()) {
                throw new InitializationError("no test classes found with @InSuite annotation value '" + suiteName.value() + "'");
            }
            this.runners = runnersFor(suiteName, classes);
        } catch (InitializationError e) {
            throw e;
        } catch (Exception e) {
            throw new InitializationError(e);
        }
    }

    @Override
    protected Description describeChild(final Runner child) {
        return child.getDescription();
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

    @Override
    protected void runChild(final Runner child, final RunNotifier notifier) {
        child.run(notifier);
    }

    private List<Runner> runnersFor(SuiteName suiteName, final Collection<Class<?>> classes) throws InitializationError {
        final List<Runner> runners = new ArrayList<Runner>();
        for (final Class<?> testClass : classes) {
            final InSuite classInSuite = testClass.getAnnotation(InSuite.class);
            final List<FrameworkMethod> testMethods = new ArrayList<FrameworkMethod>();
            for (final Method method : testClass.getMethods()) {
                if (isRunnable(suiteName, classInSuite, method)) {
                    testMethods.add(new FrameworkMethod(method));
                }
            }
            if (!testMethods.isEmpty()) {
                runners.add(new BlockJUnit4ClassRunner(testClass) {
                    @Override
                    public List<FrameworkMethod> computeTestMethods() {
                        return testMethods;
                    }
                });
            }
        }
        return runners;
    }

    private boolean isRunnable(SuiteName suiteName, final InSuite classInSuite, final Method method) throws InitializationError {
        final boolean isTestMethod = (method.isAnnotationPresent(Test.class)) &&
                (method.getParameterTypes().length == 0) &&
                (method.getReturnType().equals(Void.TYPE));
        if (!isTestMethod) {
            return false;
        }
        return Helpers.methodInSuite(method, classInSuite, suiteName);
    }
}
