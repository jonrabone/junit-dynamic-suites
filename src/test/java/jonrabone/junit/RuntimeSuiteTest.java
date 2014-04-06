package jonrabone.junit;

import jonrabone.junit.examples.TestSuiteOne;
import jonrabone.junit.examples.TestSuiteTwo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RuntimeSuiteTest {

    private Listener listener;
    private RunNotifier notifier;

    @Before
    public void setUp() {
        notifier = new RunNotifier();
        listener = new Listener();
        notifier.addListener(listener);
    }

    @Test
    public void testSuiteOne() throws Exception {
        listener.expect("suiteOneTestA", "suiteOneTestB", "anotherSuiteOneTest", "duplicateSuiteOneTest");
        runAndVerify(TestSuiteOne.class);
    }

    @Test
    public void testSuiteTwo() throws Exception {
        listener.expect("suiteTwoTest", "anotherSuiteTwoTest");
        runAndVerify(TestSuiteTwo.class);
    }

    private void runAndVerify(Class<?> testClass) throws Exception {
        RuntimeSuite suite = new RuntimeSuite(testClass);
        listener.setSuite(testClass.getSimpleName());
        for (Runner runner : suite.getChildren()) {
            runner.run(notifier);
        }
        listener.verify();
    }


    private class Listener extends RunListener {

        private String suite = "<not set>";
        private final Set<String> expectedMethodNames = new HashSet<String>();
        private final Set<String> actualMethodNames = new HashSet<String>();

        public void setSuite(String suite) {
            this.suite = suite;
        }

        public void expect(String... names) {
            expectedMethodNames.addAll(Arrays.asList(names));
        }

        public void verify() {
            assertThat(actualMethodNames, equalTo(expectedMethodNames));
        }

        @Override
        public void testRunStarted(Description description) throws Exception {
            System.out.println("-------------------------------------------------");
            System.out.println(description.getDisplayName());
        }

        @Override
        public void testStarted(Description description) throws Exception {
            System.out.println(description.getDisplayName());
            actualMethodNames.add(description.getMethodName());
        }
    }
}
