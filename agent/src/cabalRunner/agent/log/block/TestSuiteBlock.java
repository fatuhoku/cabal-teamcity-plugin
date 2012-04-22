package cabalRunner.agent.log.block;

import jetbrains.buildServer.agent.BuildProgressLogger;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [created by: H.Poon on: 09/04/2012 at: 16:48]
 */
public class TestSuiteBlock extends Block {
    public TestSuiteBlock() {
        super("test-suite");
    }

    @Override
    protected String getBlockType() {
        return LogConstants.BLOCK_TYPE_CABAL_TEST_SUITE;
    }

    @Override
    public Pattern beginPattern(String indent) {
        return Pattern.compile(indent+"Test suite ([^:]*): RUNNING\\.\\.\\.");
    }

    @Override
    public Pattern endPattern(String indent) {
        return Pattern.compile(indent+"Test suite ([^:]*): (PASS|FAIL|SKIPPED)");
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

    // We expect both the captured groups' values to be equivalent.
    @Override
    public void onBlockBegin(Matcher matcher, Map<String, String> properties, BuildProgressLogger logger) {
        // System.out.println("BEGIN test-suite ["+capturedName(matcher)+"]");
        logger.logSuiteStarted(capturedName(matcher));
    }

    @Override
    public void onBlockEnd(MatchedBlock block, Matcher matcher, Map<String, String> properties, BuildProgressLogger logger) {
        // System.out.println("END test-suite ["+block.name+"]");
        logger.logSuiteFinished(block.name);
    }
}
