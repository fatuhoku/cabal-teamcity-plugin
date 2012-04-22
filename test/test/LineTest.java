import org.testng.annotations.Test;
import cabalRunner.agent.log.line.CompilingLine;

import java.util.Arrays;
import java.util.List;

/**
 * [created by: H.Poon on: 09/04/2012 at: 22:47]
 */
public class LineTest {
    @Test
    public void testCompilingLineMatches() {
        List<String> examples = Arrays.asList(
                "[ 3 of 12] Compiling Block ( src/Block.hs, dist/build/get-player-coords/get-player-coords-tmp/Block.o )",
                "[ 6 of 12] Compiling NBTExtras        ( src/NBTExtras.hs, dist/build/get-player-coords/get-player-coords-tmp/NBTExtras.o )");

        CompilingLine pattern = new CompilingLine();
        for (String e : examples) {
            Asserts.assertMatches(pattern.getPattern(), e);
        }
    }
}
