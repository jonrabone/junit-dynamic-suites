package jonrabone.junit.examples;

import jonrabone.junit.InSuite;
import org.junit.Test;

@InSuite("Test Suite One")
public class DuplicateSuiteOneTest {
    @Test
    @InSuite("Test Suite One")
    public void duplicateSuiteOneTest() {
    }
}
