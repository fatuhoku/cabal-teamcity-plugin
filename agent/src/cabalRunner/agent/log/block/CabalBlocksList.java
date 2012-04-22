package cabalRunner.agent.log.block;

import java.util.Arrays;
import java.util.List;

/**
 * Here's a collection of known blocks.
 */
public class CabalBlocksList {
    static public List<Block> blocks = Arrays.asList(
            new CleanBlock(),
            new ConfigureBlock(),
            new BuildBlock(),
            new TestBlock(),
            new TestSuiteBlock(),
            new TestGroupBlock(),
            new TestCasePassBlock(),
            new TestCaseFailedBlock()
    );
}
