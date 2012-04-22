package cabalRunner.common;

import java.util.HashMap;
import java.util.Map;

import static cabalRunner.common.PluginConstants.BuildLogVerbosity;

public class PluginDefaults {
    private static final String DEFAULT_BUILD_OUTPUT_DIRECTORY = "dist";
    private static final String DEFAULT_COMPILER_NAME = "ghc";
    private static final String DEFAULT_BUILD_LOG_VERBOSITY = BuildLogVerbosity.VERBOSITY_1.toString();
    public static final int DEFAULT_CONFIGURE_VERBOSITY = 2;
    private static final String DEFAULT_TEST_REPORTING = "user";

    public static Map<String, String> populateBlankPropertiesWithDefaults(final Map<String, String> parameters) {
        // Special case: f the compiler path is not defined but the name is defined, then
        // the compiler path will default to whatever the compiler name is set to.
        // N.B.: It appears that if no string value is given for a parameter, no mapping is made.
        // i.e. parameters.get(...) will return null.
        Map<String,String> params = new HashMap<String, String>(parameters);

        String buildOutputDirectory = Parameters.getString(params, PluginConstants.PROPERTY_BUILD_OUTPUT_DIRECTORY);
        String compilerName = Parameters.getString(params, PluginConstants.PROPERTY_COMPILER_NAME);
        String compilerPath = Parameters.getString(params, PluginConstants.PROPERTY_COMPILER_PATH);
        String buildLogVerbosity = Parameters.getString(params, PluginConstants.PROPERTY_BUILD_LOG_VERBOSITY);
        String testReporting = Parameters.getString(params, PluginConstants.PROPERTY_TEST_REPORTING);

        if (buildOutputDirectory.isEmpty()) {
            params.put(PluginConstants.PROPERTY_BUILD_OUTPUT_DIRECTORY, DEFAULT_BUILD_OUTPUT_DIRECTORY);
        }
        if (compilerName.isEmpty()) {
            params.put(PluginConstants.PROPERTY_COMPILER_NAME, DEFAULT_COMPILER_NAME);
        }
        if (compilerPath.isEmpty()) {
            params.put(PluginConstants.PROPERTY_COMPILER_PATH, params.get(PluginConstants.PROPERTY_COMPILER_NAME));
        }
        if (buildLogVerbosity.isEmpty()) {
            params.put(PluginConstants.PROPERTY_BUILD_LOG_VERBOSITY, DEFAULT_BUILD_LOG_VERBOSITY);
        }
        if (testReporting.isEmpty()) {
            params.put(PluginConstants.PROPERTY_TEST_REPORTING, DEFAULT_TEST_REPORTING);
        }
        return params;
    }
}
