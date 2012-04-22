package cabalRunner.agent;

import org.apache.commons.io.FilenameUtils;
import cabalRunner.common.Parameters;

import java.util.Map;

import static cabalRunner.common.PluginConstants.*;

/**
 * This class contains parameters options that modify the behaviour of the various CabalRunners.
 * Where CabalRunnerParameters reflect CabalRunners are operating system specific.
 */
public class CabalRunnerParameters {

    final String buildOutputDirectory;
    final String compilerName;
    final String compilerPath;
    final BuildLogVerbosity buildLogVerbosity;
    final String compilerFlags;
    final boolean cleanBefore;
    final boolean runTests;
    final String testReporting;

    public CabalRunnerParameters(Map<String, String> runnerParameters) {
        this.compilerName = Parameters.getString(runnerParameters, PROPERTY_COMPILER_NAME);
        this.compilerPath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PROPERTY_COMPILER_PATH));
        this.compilerFlags = Parameters.getString(runnerParameters, PROPERTY_COMPILER_FLAGS);
        this.buildLogVerbosity = BuildLogVerbosity.fromString(
                Parameters.getString(runnerParameters, PROPERTY_BUILD_LOG_VERBOSITY));
        this.buildOutputDirectory = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PROPERTY_BUILD_OUTPUT_DIRECTORY));
        this.cleanBefore = Parameters.getBoolean(runnerParameters, PROPERTY_CLEAR_OUTPUT_BEFORE);
        this.runTests = Parameters.getBoolean(runnerParameters, PROPERTY_RUN_TESTS);
        this.testReporting = Parameters.getString(runnerParameters, PROPERTY_TEST_REPORTING);
    }
}


