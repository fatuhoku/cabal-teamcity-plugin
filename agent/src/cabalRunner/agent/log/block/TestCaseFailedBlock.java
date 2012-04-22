package cabalRunner.agent.log.block;

import jetbrains.buildServer.agent.BuildProgressLogger;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test case descriptions necessarily must start with a non-whitespace character
 * for test-reporting to work correctly.
 */
public class TestCaseFailedBlock extends Block {

    static final String failedTestCasePattern = "(\\S.*): \\[Failed\\]";

    public TestCaseFailedBlock() {
        super("test-case-failed");
        shouldConsumeEnd = false; // Make final where possible.
    }

    @Override
    protected String getBlockType() {
        return LogConstants.BLOCK_TYPE_CABAL_TEST_CASE;
    }

    @Override
    public Pattern beginPattern(String indent) {
        return Pattern.compile(indent + failedTestCasePattern);
    }

    /**
     * We close off test cases as we see other test cases beginning.
     * @param indent
     * @return
     */
    @Override
    public Pattern endPattern(String indent) {
        return Pattern.compile(indent + String.format("(?:%s|%s)", failedTestCasePattern, TestCasePassBlock.passTestCasePattern));
    }

    @Override
    public String beginLine() {
        return "beginLine is disabled";
    }

    @Override
    public String endLine() {
        return "endLine is disabled";
    }

    @Override
    public String capturedName(Matcher matcher) {
        return matcher.group(1);
    }

    /**
     * Capture the test matchIndentation/description.
     *
     * @param matcher
     * @param properties
     * @param logger
     */
    @Override
    public void onBlockBegin(Matcher matcher, Map<String, String> properties, BuildProgressLogger logger) {
        System.out.println("BEGIN test-fail-case ["+capturedName(matcher)+"]");
        logger.logTestStarted(capturedName(matcher));
    }

    /**
     * Capture the test matchIndentation/description. See endPattern() to understand the magic number.
     *
     * @param matcher
     * @param properties
     * @param logger
     */
    @Override
    public void onBlockEnd(MatchedBlock block, Matcher matcher, Map<String, String> properties, BuildProgressLogger logger) {
        System.out.println("END test-fail-case ["+block.name+"]");
        logger.logTestFailed(block.name, "Test failed.", "");
    }
}
