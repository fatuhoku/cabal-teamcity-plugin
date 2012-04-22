package cabalRunner.common;

import org.jetbrains.annotations.NonNls;

public interface PluginConstants {
    @NonNls final String RUN_TYPE = "cabalRunner";
    @NonNls final String RUNNER_DISPLAY_NAME = "Cabal";
    final String RUNNER_DESCRIPTION = "Haskell Cabal runner";

    final String PROPERTY_BUILD_OUTPUT_DIRECTORY = "argument.build_output_directory";
    final String PROPERTY_COMPILER_NAME = "argument.compiler_name";
    final String PROPERTY_COMPILER_PATH = "argument.compiler_path";
    final String PROPERTY_BUILD_LOG_VERBOSITY = "argument.build_log_verbosity";

    final String PROPERTY_COMPILER_FLAGS = "argument.compiler_flags";
    final String PROPERTY_CLEAR_OUTPUT_BEFORE = "argument.clean_before_build";
    final String PROPERTY_RUN_TESTS = "argument.run_tests";

    // Can either be 'user' or 'test-framework'. 'user' requires the user to send Teamcity service messages.
    final String PROPERTY_TEST_REPORTING = "argument.test_reporting";
    final String TEST_REPORTING_USER = "user";
    final String TEST_REPORTING_TEST_FRAMEWORK = "test-framework";

    // Agent properties that the agent discovers
    final String AGENT_CONFIGURATION_CABAL_PATH = "cabal.path";
    final String AGENT_CONFIGURATION_CABAL_VERSION = "cabal.version";

    public enum BuildLogVerbosity {
        VERBOSITY_0("verbosity-0"),
        VERBOSITY_1("verbosity-1"),
        VERBOSITY_2("verbosity-2"),
        VERBOSITY_3("verbosity-3");
        
        private String representation;

        BuildLogVerbosity(String representation) {
            this.representation = representation;
        }
        
        public String toString () {
            return representation;
        }

        public static BuildLogVerbosity fromString(String str) {
            if("verbosity-0".equals(str)) {
                return VERBOSITY_0;
            } else if ("verbosity-1".equals(str)) {
                return VERBOSITY_1;
            } else if ("verbosity-2".equals(str)) {
                return VERBOSITY_2;
            } else if ("verbosity-3".equals(str)) {
                return VERBOSITY_3;
            }
            throw new IllegalArgumentException(str+" is not a recognised build log verbosity level.");
        }
    }
}
