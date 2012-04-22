import org.testng.annotations.Test;
import cabalRunner.agent.log.block.LogConstants;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * [created by: H.Poon on: 09/04/2012 at: 22:56]
 */
public class PatternsTest {
    @Test
    public void testRelativePathPattern() {
        List<String> testPaths = Arrays.asList("src/Block.hs", "dist/build/get-player-coords/get-player-coords-tmp/Block.o");
        for (String testPath : testPaths) {
            Asserts.assertMatches(Pattern.compile(LogConstants.PATTERN_REL_PATH), testPath);
        }
    }

    @Test
    public void testAlphaNumeric() {
        Asserts.assertMatches(Pattern.compile("\\w+"), "ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmopqrstuvwxyz0123456789");
    }

}
