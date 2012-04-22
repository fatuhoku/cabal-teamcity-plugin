package cabalRunner.agent.log.line;

import java.util.Arrays;
import java.util.List;

public class CabalLogLines {
    public static List<Line> list = Arrays.asList(
            new CompilingLine(),
            new TestGroupLine()
    );
}