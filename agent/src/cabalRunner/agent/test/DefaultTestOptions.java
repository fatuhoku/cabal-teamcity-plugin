package cabalRunner.agent.test;

import cabalRunner.agent.test.options.TestOptions;

/**
 * If the user chooses to do test reporting him/herself through TeamCity service messages
 * then there is nothing we can pass on to the test executable.
 */
public class DefaultTestOptions extends TestOptions {
    @Override
    public String testOptions() {
        return "";
    }
}
