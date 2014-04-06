package jonrabone.junit.examples;

import jonrabone.junit.InSuite;
import org.junit.Test;

@InSuite("Test Suite One")
public class BrokenSuiteOneTest {
    @Test
    @InSuite("Test Suite Two")
    public void brokenSuiteTwoTest() {
    }
}
