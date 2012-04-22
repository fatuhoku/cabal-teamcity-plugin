package cabalRunner.agent.log.block;

import java.util.regex.Pattern;

/**
 * Captures all output that occurs during a 'cabal test'.
 */
public class TestBlock extends Block {

    public TestBlock() {
        super("test");
    }

    @Override
    protected String getBlockType() {
        return LogConstants.BLOCK_TYPE_CABAL_TEST;
    }

    @Override
    public String blockIndentation() {
        return ">>> ";
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
    public Pattern beginPattern(String indent) {
        return Pattern.compile(String.format("%sRunning %s test suites\\.\\.\\.", indent, LogConstants.PATTERN_NAT_NUM));
    }

    @Override
    public Pattern endPattern(String indent) {
        return Pattern.compile(String.format(
                "%1$s%2$s of %2$s test suites \\(%2$s of %2$s test cases\\) passed.",
                indent, LogConstants.PATTERN_NAT_NUM)
        );
    }
}
