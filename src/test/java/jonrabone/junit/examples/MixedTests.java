package jonrabone.junit.examples;

import jonrabone.junit.InSuite;
import org.junit.Test;

public class MixedTests {
    @Test
    public void notInAnySuiteTest() {
    }

    @Test
    @InSuite("Test Suite One")
    public void anotherSuiteOneTest() {
    }

    @Test
    @InSuite("Test Suite Two")
    public void anotherSuiteTwoTest() {
    }
}
