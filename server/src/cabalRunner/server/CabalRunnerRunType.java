package cabalRunner.server;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;
import cabalRunner.common.Parameters;
import cabalRunner.common.PluginConstants;
import cabalRunner.common.PluginDefaults;

import java.util.*;

public class CabalRunnerRunType extends RunType {
    public CabalRunnerRunType(final RunTypeRegistry registry) {
        registry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return PluginConstants.RUN_TYPE;
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return PluginConstants.RUNNER_DISPLAY_NAME;
    }

    @Override
    public String getDescription() {
        return PluginConstants.RUNNER_DESCRIPTION;
    }


    @Override
    @NotNull
    public String describeParameters(@NotNull final Map<String, String> parameters)
    {
        Map<String, String> params = PluginDefaults.populateBlankPropertiesWithDefaults(parameters);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Build output directory: ");
        sb.append(params.get(PluginConstants.PROPERTY_BUILD_OUTPUT_DIRECTORY));
        sb.append(" \n");
        sb.append("Compiler: ");
        sb.append(params.get(PluginConstants.PROPERTY_COMPILER_NAME));
        sb.append(" \n");

        // Only show the path if it is different from the compiler name.
        String compilerPath = Parameters.getString(params, PluginConstants.PROPERTY_COMPILER_PATH);
        if (!compilerPath.equals(params.get(PluginConstants.PROPERTY_COMPILER_NAME))) {
            sb.append("Compiler path: ");
            sb.append(params.get(PluginConstants.PROPERTY_COMPILER_PATH));
            sb.append(" \n");
        }

        sb.append("Build log verbosity: ");
        sb.append(PluginConstants.BuildLogVerbosity.fromString(params.get(PluginConstants.PROPERTY_BUILD_LOG_VERBOSITY)).ordinal());
        sb.append(" \n");

        boolean runTests = Parameters.getBoolean(params, PluginConstants.PROPERTY_RUN_TESTS);
        sb.append("Run tests: ");
        sb.append(runTests);
        sb.append(" \n");
        if(runTests) {
            sb.append("Test reporting: ");
            sb.append(Parameters.getString(params, PluginConstants.PROPERTY_TEST_REPORTING));
            sb.append(" \n");
        }

        // Only show flags if there are any
        String compilerFlags = Parameters.getString(params, PluginConstants.PROPERTY_COMPILER_FLAGS);
        if (!compilerFlags.isEmpty()) {
            sb.append("Compiler flags: ");
            sb.append(compilerFlags);
        }

        return sb.toString();
    }

    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor()
    {
        return new PropertiesProcessor()
        {
            @Override
            public Collection<InvalidProperty> process(Map<String, String> properties)
            {
                // There is only one error case: whenever the path exists, a compiler name must be given.
                String compilerName = properties.get(PluginConstants.PROPERTY_COMPILER_NAME);
                String compilerPath = properties.get(PluginConstants.PROPERTY_COMPILER_PATH);
                if (compilerPath != null && !compilerPath.isEmpty() && (compilerName == null || compilerName.isEmpty())) {
                    return Arrays.asList(new InvalidProperty(PluginConstants.PROPERTY_COMPILER_NAME, "You must give a compiler name whenever you specify a compiler path."));
                }
                return Collections.emptySet();
            }
        };
    }

    @Override
    public String getEditRunnerParamsJspFilePath() {
        return "editRunnerRunParameters.jsp";
    }

    @Override
    public String getViewRunnerParamsJspFilePath() {
        return "viewRunnerRunParameters.jsp";
    }

    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        Map<String,String> defaults = new HashMap<String, String>();

        defaults.put(PluginConstants.PROPERTY_BUILD_LOG_VERBOSITY,
                PluginConstants.BuildLogVerbosity.VERBOSITY_1.toString());
        defaults.put(PluginConstants.PROPERTY_TEST_REPORTING,
                PluginConstants.TEST_REPORTING_USER);
        defaults.put(PluginConstants.PROPERTY_CLEAR_OUTPUT_BEFORE, "true");

        return defaults;
    }
}
