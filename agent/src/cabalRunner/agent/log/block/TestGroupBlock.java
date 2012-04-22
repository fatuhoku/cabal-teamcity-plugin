package cabalRunner.agent.log.block;

import jetbrains.buildServer.agent.BuildProgressLogger;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test group dsecriptions necessarily must start with a non-whitespace character
 * for test-reporting to work correctly.
 * TODO Abstract out details like indentation into a TestFrameworkConfiguration object
 * to allow flexible configuration for different test output formats from different versions of test-framework.
 */
public class TestGroupBlock extends Block {

    static String testGroupPattern = "(\\S.*):";

    public TestGroupBlock() {
        super("test-group");
        shouldConsumeEnd = false; // Make final where possible.
    }

    @Override
    protected String getBlockType() {
        return LogConstants.BLOCK_TYPE_CABAL_TEST_SUITE;
    }

    @Override
    public String blockIndentation() {
        return "  ";
    }

    @Override
    public Pattern beginPattern(String indent) {
        return Pattern.compile(indent + testGroupPattern);
    }

    /**
     * We close off test groups as new groups begin...
     * @param indent
     * @return
     */
    @Override
    public Pattern endPattern(String indent) {
        return Pattern.compile(indent + testGroupPattern);
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

    @Override
    public void onBlockBegin(Matcher matcher, Map<String, String> properties, BuildProgressLogger logger) {
        // System.out.println("BEGIN test-group ["+capturedName(matcher)+"]");
        logger.logSuiteStarted(capturedName(matcher));
    }

    @Override
    public void onBlockEnd(MatchedBlock block, Matcher matcher, Map<String, String> properties, BuildProgressLogger logger) {
        // System.out.println("END test-group ["+block.name+"]");
        logger.logSuiteFinished(block.name);
    }
}
