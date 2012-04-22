package cabalRunner.agent.test.options;

/**
 * If the test executable is built with test-framework, then we must ensure
 * the test reporting output is free of terminal colours or fancy terminal features such as
 * the use of carriage return.
 */
public class TestFrameworkTestOptions extends TestOptions {
    @Override
    public String testOptions() {
        return "--plain";
    }
}
