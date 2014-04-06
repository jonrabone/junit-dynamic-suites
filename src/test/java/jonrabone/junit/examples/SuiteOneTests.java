package jonrabone.junit.examples;

import jonrabone.junit.InSuite;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@InSuite("Test Suite One")
public class SuiteOneTests {

    private boolean setup = false;

    @Before
    public void setUp() throws Exception {
        setup = true;
    }

    @Test
    public void suiteOneTestA() {
        assertThat(setup, equalTo(true));
    }

    @Test
    public void suiteOneTestB() {
        assertThat(setup, equalTo(true));
    }
}
