import cabalRunner.agent.log.CabalLogListener;
import cabalRunner.agent.log.block.*;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildProgressLogger;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * [created by: H.Poon on: 09/04/2012 at: 14:54]
 */
public class BlockTest {
    List<String> testGroupLines = Arrays.asList(
            ">>> Main:",
            ">>> Unit tests:",
            ">>> Properties:");

    List<String> testCaseLines = Arrays.asList(
            ">>>   propDecEncRegion: [OK, passed 100 tests]",
            ">>>   DecodeEncodeRegion: [OK, passed 100 tests]",
            ">>>   DecodeEncodeBlockIds: [OK, passed 100 tests]",
            ">>>   DecodeEncodeBlockData: [OK, passed 100 tests]",
            ">>>   ToFromNybbles: [OK, passed 100 tests]",
            ">>>   MoveToBlockIdWorks: [OK]",
            ">>>   MoveToDataTagWorks: [OK]",
            ">>>   UpdateChunkUpdatesBlockIds: [OK]",
            ">>>   UpdateChunkUpdatesBlockData: [OK]",
            ">>>   OneBlockPlacesCorrectlyColouredWoolInWorld: [OK]");

    List<String> testBlankLines = Arrays.asList(
            ">>> ",
            ">>>  ",
            ">>> \t",
            ">>>  \t");

    String suiteReport =
            "Test suite test-lshift-minecraft: RUNNING...\n" +
            "Properties:\n" +
            "  propDecEncRegion: [OK, passed 100 tests]\n" +
            "  propToFromNybbles: [OK, passed 100 tests]\n" +
            "  propDecEncBlockIds: [OK, passed 100 tests]\n" +
            "  propDecEncBlockData: [OK, passed 100 tests]\n" +
            "Unit tests:\n" +
            "  moveToTag:\n" +
            "    :Move to Blocks tag: [OK]\n" +
            "    :Move to Data tag: [OK]\n" +
            "    :Put wool block in gold chunk: [OK]\n" +
            "    :Put different coloured wool block in wool chunk!: [OK]\n" +
            "Reading level...\n" +
            "Extracted coordinates...\n" +
            "  player: PlayerCoords (-48,75,194)\n" +
            "Updating region...\n" +
            "Done!\n" +
            "The observed block Id is: 35\n" +
            "The observed block data is: 15\n" +
            "    :Modify the world, and observe that it has changed: [OK]\n" +
            "\n" +
            "         Properties  Test Cases  Total      \n" +
            " Passed  4           5           9          \n" +
            " Failed  0           0           0          \n" +
            " Total   4           5           9          \n" +
            "Test suite test-lshift-minecraft: PASS\n";


    String suiteCompleteReport =
            "Running 1 test suites...\n" +
            "Test suite test-cabal-demo: RUNNING...\n" +
            ">>> Test suite test-cabal-demo: RUNNING...\n" +
            ">>> Cases::\n" +
            ">>>   Failure: [Failed]\n" +
            ">>> Failed: This test was designed to fail 1.\n" +
            ">>>   Failure: [Failed]\n" +
            ">>> Failed: This test was designed to fail 2.\n" +
            ">>>   Failure: [Failed]\n" +
            ">>> Failed: This test was designed to fail 3.\n" +
            ">>>   Success: [OK]\n" +
            ">>>   Success: [OK]\n" +
            ">>> Properties::\n" +
            ">>> 1\n" +
            ">>>   Failure: [Failed]\n" +
            ">>> Falsifiable with seed 6359701511992572685, after 20 tests. Reason: Falsifiable\n" +
            ">>> 1\n" +
            ">>>   Failure: [Failed]\n" +
            ">>> Falsifiable with seed -8316090164245041104, after 30 tests. Reason: Falsifiable\n" +
            ">>> 1\n" +
            ">>>   Failure: [Failed]\n" +
            ">>> Falsifiable with seed -357125393799130889, after 40 tests. Reason: Falsifiable\n" +
            ">>>   Success: [OK, passed 100 tests]\n" +
            ">>>   Success: [OK, passed 100 tests]\n" +
            ">>> \n" +
            ">>>          Properties   Test Cases   Total       \n" +
            ">>>  Passed  7            6            13          \n" +
            ">>>  Failed  9            28           37          \n" +
            ">>>  Total   16           34           50   \n" +
            ">>> Test suite test-cabal-demo: FAIL\n" +
            ">>> Test suite logged to: dist/test/cabal-plugin-demo-0.1-test-cabal-demo.log\n" +
            "Test suite test-cabal-demo: FAIL\n" +
            "Test suite logged to: dist/test/cabal-plugin-demo-0.1-test-cabal-demo.log\n" +
            "0 of 1 test suites (0 of 1 test cases) passed.";

    String suiteCompleteReport2 =
            "Running 1 test suites...\n" +
                    "Test suite test-cabal-demo: RUNNING...\n" +
                    ">>> Test suite test-cabal-demo: RUNNING...\n" +
                    ">>> Cases::\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Failed: This test was designed to fail.\n" +
                    ">>>   Success: [OK]\n" +
                    ">>>   Success: [OK]\n" +
                    ">>>   Success: [OK]\n" +
                    ">>>   Success: [OK]\n" +
                    ">>>   Success: [OK]\n" +
                    ">>>   Success: [OK]\n" +
                    ">>> Properties::\n" +
                    ">>> 1\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Falsifiable with seed 6359701511992572685, after 2 tests. Reason: Falsifiable\n" +
                    ">>> 1\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Falsifiable with seed -8316090164245041104, after 4 tests. Reason: Falsifiable\n" +
                    ">>> 1\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Falsifiable with seed 800725499979108993, after 2 tests. Reason: Falsifiable\n" +
                    ">>> 1\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Falsifiable with seed 9123688446068452795, after 2 tests. Reason: Falsifiable\n" +
                    ">>> 1\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Falsifiable with seed 2775132580268863907, after 2 tests. Reason: Falsifiable\n" +
                    ">>> 1\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Falsifiable with seed 2527726138181788245, after 3 tests. Reason: Falsifiable\n" +
                    ">>> 1\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Falsifiable with seed -2282148340978418751, after 2 tests. Reason: Falsifiable\n" +
                    ">>> 1\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Falsifiable with seed 4371446522269525764, after 2 tests. Reason: Falsifiable\n" +
                    ">>> 1\n" +
                    ">>>   Failure: [Failed]\n" +
                    ">>> Falsifiable with seed -357125393799130889, after 3 tests. Reason: Falsifiable\n" +
                    ">>>   Success: [OK, passed 100 tests]\n" +
                    ">>>   Success: [OK, passed 100 tests]\n" +
                    ">>>   Success: [OK, passed 100 tests]\n" +
                    ">>>   Success: [OK, passed 100 tests]\n" +
                    ">>>   Success: [OK, passed 100 tests]\n" +
                    ">>>   Success: [OK, passed 100 tests]\n" +
                    ">>>   Success: [OK, passed 100 tests]\n" +
                    ">>> \n" +
                    ">>>          Properties   Test Cases   Total       \n" +
                    ">>>  Passed  7            6            13          \n" +
                    ">>>  Failed  9            28           37          \n" +
                    ">>>  Total   16           34           50          \n" +
                    ">>> Test suite test-cabal-demo: FAIL\n" +
                    ">>> Test suite logged to: dist/test/cabal-plugin-demo-0.1-test-cabal-demo.log\n" +
                    "Test suite test-cabal-demo: FAIL\n" +
                    "Test suite logged to: dist/test/cabal-plugin-demo-0.1-test-cabal-demo.log\n" +
                    "0 of 1 test suites (0 of 1 test cases) passed.";


    @Test
    public void testBlockBeginEnd() {
        // For blocks that haven't overridden the startLine / endLine methods,
        // their pattern should match the line given.
        // i.e. they look like ##cabalrunner begin[xxx]
        List<Block> blocks = CabalBlocksList.blocks;

        for (Block b : blocks) {
            String beginLine = b.beginLine();
            String endLine = b.endLine();
            if(beginLine.startsWith("##cabalrunner begin[")) {
                assertMatchesBegin(b, "", b.beginLine());
            }
            if(endLine.startsWith("##cabalrunner end[")) {
                assertMatchesEnd(b, "", b.endLine());
            }
        }
    }

    @Test
    public void testTestBlockBeginEnd() {
        String exampleBegin = "Running 11289 test suites...";
        String antiExampleBegin = "Running 11289 test suitesabc";
        String exampleEnd = "1135 of 11289 test suites (14 of 189 test cases) passed.";

        Block testBlock = new TestBlock();
        assertMatchesBegin(testBlock, "", exampleBegin);
        assertNotMatchesBegin(testBlock, "", antiExampleBegin);
        assertMatchesEnd(testBlock, "", exampleEnd);
    }

    @Test
    public void testTestSuiteBlockBeginEnd() {
        String exampleBegin = "Test suite testSuiteWithCamelCase: RUNNING...";
        String antiExampleBegin = "Test suite TEST_SUITE_WITH_UNDERSCORES: RUNNINGabc";
        String exampleEnd1 = "Test suite testSuiteWith3_numbers.1.2: PASS";
        String exampleEnd2 = "Test suite test-suite-with-hypens: FAIL";

        Block testSuiteBlock = new TestSuiteBlock();
        assertMatchesBegin(testSuiteBlock, "", exampleBegin);
        assertNotMatchesBegin(testSuiteBlock, "", antiExampleBegin);
        assertMatchesEnd(testSuiteBlock, "", exampleEnd1);
        assertMatchesEnd(testSuiteBlock, "", exampleEnd2);

        Matcher m1 = testSuiteBlock.beginMatcher("", exampleBegin);
        m1.matches();
        assertEquals("testSuiteWithCamelCase", m1.group(1));

        Matcher m2 = testSuiteBlock.endMatcher("", exampleEnd1);
        m2.matches();
        assertEquals("testSuiteWith3_numbers.1.2", m2.group(1));

        Matcher m3 = testSuiteBlock.endMatcher("", exampleEnd2);
        m3.matches();
        assertEquals("test-suite-with-hypens", m3.group(1));
    }

    @Test
    public void testTestGroupBlockBeginEnd() {
        Block testGroupBlock = new TestGroupBlock();
        for (String line : testGroupLines) {
            assertMatchesBegin(testGroupBlock, ">>> ", line);
            assertMatchesEnd(testGroupBlock, ">>> ", line);
        }
    }

    @Test
    public void testTestCaseBlockBeginEnd() {
        Block testCaseBlock = new TestCasePassBlock();
        for (String line : testCaseLines) {
            assertMatchesBegin(testCaseBlock, ">>>   ", line);
            assertMatchesEnd(testCaseBlock, ">>>   ", line);
        }
    }

    @Test
    public void testUnmatchable() {
        Pattern pattern = Pattern.compile(LogConstants.PATTERN_UNMATCHABLE);
        assertFalse(pattern.matcher("unmatchable").matches());
    }

    @Test
    public void testWtf() {
        assertTrue(Pattern.compile(">>> (\\s*|([^:]+):)").matcher(">>> ").matches());
    }

    @Test
    public void testGroup() {
        assertTrue(Pattern.compile("\\S.*:").matcher("Main:").matches());
        assertTrue(Pattern.compile("\\S.*:").matcher("Properties:").matches());
        assertTrue(Pattern.compile("\\S.*:").matcher("Unit tests:").matches());
        assertTrue(Pattern.compile("\\S.*:").matcher(":Test group name beginning with colon with %&^%$ symbols:").matches());
        assertFalse(Pattern.compile("\\S.*:").matcher(" Main:").matches());
    }

    @Test
    public void testLogListener() {
        BuildAgentConfiguration config = mock(BuildAgentConfiguration.class);
        BuildProgressLogger logger = mock(BuildProgressLogger.class);
        CabalLogListener listener = new CabalLogListener(config, logger);

        for (String reportLine : suiteReport.split("\n")){
            listener.onStandardOutput(reportLine);
        }

        // Once for lshift-minecraft test-suite, two for each of { "Properties", "Unit tests" } test groups
        ArgumentCaptor<String> beginSuiteNames = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> endSuiteName = ArgumentCaptor.forClass(String.class);
        verify(logger, times(4)).logSuiteStarted(beginSuiteNames.capture());
        verify(logger, times(4)).logSuiteFinished(endSuiteName.capture());

        // Make assertions: sequence of exits should be something like
        // Properties, MoveToTag, Unit tests, test-lshift-minecraft.
        // i.e. [1, 3, 2, 0]

        List<String> expected = new ArrayList<String>();
        List<String> beginLines = beginSuiteNames.getAllValues();
        expected.add(beginLines.get(1)); // Properties
        expected.add(beginLines.get(3)); // moveToTag
        expected.add(beginLines.get(2)); // Unit tests
        expected.add(beginLines.get(0)); // test-lshift-minecraft

        assertEquals(expected, endSuiteName.getAllValues());

        verify(logger, times(9)).logTestStarted(anyString());
        verify(logger, times(9)).logTestFinished(anyString());
        verify(logger, never()).logTestFailed(anyString(), any(Throwable.class));
        verify(logger, never()).logTestFailed(anyString(), anyString(), anyString());
    }


    @Test
    public void testLogListener2() {
        BuildAgentConfiguration config = mock(BuildAgentConfiguration.class);
        BuildProgressLogger logger = mock(BuildProgressLogger.class);
        CabalLogListener listener = new CabalLogListener(config, logger);

        for (String reportLine : suiteCompleteReport2.split("\n")){
            listener.onStandardOutput(reportLine);
        }

        ArgumentCaptor<String> beginSuiteNames = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> endSuiteName = ArgumentCaptor.forClass(String.class);
        verify(logger, times(3)).logSuiteStarted(beginSuiteNames.capture());
        verify(logger, times(3)).logSuiteFinished(endSuiteName.capture());

        // 28 + 6, 9 + 7
        verify(logger, times(50)).logTestStarted(anyString());
        verify(logger, times(13)).logTestFinished(anyString());
        verify(logger, never()).logTestFailed(anyString(), any(Throwable.class));
        verify(logger, times(37)).logTestFailed(anyString(), anyString(), anyString());
    }

    @Test
    public void testStack() {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.toString());
        stack.push(4);
        System.out.println(stack.toString());

        assertEquals((Integer) 1, stack.get(0));
        assertEquals((Integer) 2, stack.get(1));
        assertEquals((Integer) 3, stack.get(2));
        assertEquals((Integer) 4, stack.get(3));
    }

    // We want to ensure that indentation works as expected.

    private void assertNotMatchesBegin(Block b, String indent, String line) {
        assertFalse(b.beginMatcher(indent, line).matches(),
                String.format("\nExpected begin line: \"%s\"\nto fail match the begin pattern \"%s\" but it didn't.",
                        line, b.beginPattern(indent).toString()));
    }

    private void assertMatchesBegin(Block b, String indent, String line) {
        assertTrue(b.beginMatcher(indent, line).matches(),
                String.format("\nExpected begin line: \"%s\"\nto match the begin pattern \"%s\" but it didn't.",
                        line, b.beginPattern(indent).toString()));
    }

    private void assertMatchesEnd(Block b, String indent, String line) {
        assertTrue(b.endMatcher(indent, line).matches(),
                String.format("\nExpected endLine: \"%s\"\nto match the pattern \"%s\" but it didn't.",
                        line, b.endPattern(indent).toString()));
    }
}