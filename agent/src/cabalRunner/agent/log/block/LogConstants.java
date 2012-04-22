package cabalRunner.agent.log.block;

import jetbrains.buildServer.messages.DefaultMessagesInfo;

/**
 * [created by: H.Poon on: 09/04/2012 at: 14:28]
 * Teamcity build output log types.
 */
public class LogConstants {
    // Custom block types with CUSTOM_ prefix
    // TODO check whether TEST_CASE has a built in type.
    public static final String BLOCK_TYPE_CABAL_CLEAN = "CUSTOM_BLOCK_TYPE_CABAL_CLEAN";
    public static final String BLOCK_TYPE_CABAL_CONFIGURE = "CUSTOM_BLOCK_TYPE_CABAL_CONFIGURE";
    public static final String BLOCK_TYPE_CABAL_BUILD = "CUSTOM_BLOCK_TYPE_CABAL_BUILD";
    public static final String BLOCK_TYPE_CABAL_TEST = "CUSTOM_BLOCK_TYPE_CABAL_TEST"; // cabal test
    public static final String BLOCK_TYPE_CABAL_TEST_SUITE = DefaultMessagesInfo.BLOCK_TYPE_TEST_SUITE;
    public static final String BLOCK_TYPE_CABAL_TEST_CASE = DefaultMessagesInfo.BLOCK_TYPE_TEST;
    public static final String PATTERN_BLANK = "\\s*";
    public static final String PATTERN_NAT_NUM = "[0-9]+";

    // The following pattern was adapted from
    // http://samburney.com/blog/regular-expression-match-windows-or-unix-path
    public static final String PATTERN_REL_PATH =
            "(?:.*?/|.*?\\\\)?(?:[^\\./|^\\.\\\\]+)(?:\\.(?:[^\\\\]*)|)";
    public static final String PATTERN_UNMATCHABLE = "$unmatchable";
}
