## Dynamic Suites for JUnit 4

This project allows you to quickly create JUnit test suites (eg. groups of fast tests, slow tests and integration tests) by marking classes or individual test methods with an annotation.   

## Why?

[Test suites in JUnit](https://github.com/junit-team/junit/wiki/Aggregating-tests-in-suites) (and other testing frameworks) are a pain to configure and maintain, often requiring a manual list of classes in code or an XML file. 

## How To Use

Create an empty class and add `@SuiteName` and `@RunWith` annotations. Then mark test classes or methods with `@InSuite`:

<pre>
import jonrabone.junit.RuntimeSuite;
import jonrabone.junit.SuiteName;
import org.junit.runner.RunWith;

@SuiteName("Example Suite")
@RunWith(RuntimeSuite.class)
public class MyTestSuite {
}
</pre>

<pre>
import jonrabone.junit.InSuite;
import org.junit.Test;
import org.junit.Before;

@InSuite("Example Suite")
public class SomeSuiteTests {

    @Before
    public void setUp() throws Exception {
        ...
    }

    @Test
    public void testA() throws Exception {
        ...
    }

    @Test
    public void testB() throws Exception {
        ...
    }
}
</pre>

If you create a conflicting situation with the annotations (eg. a test method has a different `@InSuite` annotation to the parent class) an error is printed to standard error at runtime: 

`ignoring mismatched suite annotation on method public void jonrabone.junit.examples.BrokenSuiteOneTest.brokenSuiteTwoTest() : entire class marked with suite(s) [Test Suite One]`

## Installation

This project uses Maven. The simplest way to install it is to download it, run `mvn install` and then reference it in your project POM with:

<pre>
&lt;dependency&gt;
  &lt;groupId&gt;jonrabone&lt;/groupId&gt;
  &lt;artifactId&gt;junit-dynamic-suites&lt;/artifactId&gt;
  &lt;version&gt;1.0-SNAPSHOT&lt;/version&gt;
&lt;/dependency&gt;
</pre>

Alternatively you can just add the JAR files to your project. You will also need JUnit (>= 4.11), scannotation (1.0.3) and javassist (3.12.1.GA) JARs.

## Important Information About Scannotation

This project uses Scannotation for fast scanning of annotations in large projects. Unfortunately the current (1.0.3) Maven release of Scannotation does not contain a vital bug fix to handle file paths with spaces, therefore this project provides its own implementation of `org.scannotation.archiveiterator.FileProtocolIteratorFactory` as a workaround. This has been reported as issue [RESTEASY-1048](https://issues.jboss.org/browse/RESTEASY-1048) since it appears to be the JBoss RESTEasy team who uploaded it to Maven Central.


## API Reference

Please see the [site documentation](http://jonrabone.github.io/junit-dynamic-suites).

## Tests

Unit tests are included in the source; see the examples folder for a contrived example containing tests in two different suites, plus a misconfigured test and an "orphaned" test. The `RuntimeSuiteTest` class is a functional test for the project, and asserts that the various test methods are run as expected.

## TODO

There are currently no beforeSuite / afterSuite annotations. JUnit `@Before` and `@After` annotations are run as expected, but they are scoped to the parent class of the tests. Be careful if splitting a test suite across multiple class files.   

## License

This project is open source software licensed under the BSD 2-Clause License. See the file `LICENSE.txt` in the source code for details.