package cabalRunner.agent;

import cabalRunner.agent.log.block.Block;
import cabalRunner.agent.log.block.BuildBlock;
import cabalRunner.agent.log.block.CleanBlock;
import cabalRunner.agent.log.block.ConfigureBlock;
import cabalRunner.agent.os.OperatingSystemSpecifics;
import cabalRunner.agent.test.DefaultTestOptions;
import cabalRunner.agent.test.options.TestFrameworkTestOptions;
import cabalRunner.agent.test.options.TestOptions;
import org.jetbrains.annotations.NotNull;
import cabalRunner.common.PluginDefaults;
import cabalRunner.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static cabalRunner.common.PluginConstants.TEST_REPORTING_TEST_FRAMEWORK;
import static cabalRunner.common.PluginConstants.TEST_REPORTING_USER;

/**
 * Responsible for providing the various command line parameters, including providing the correct
 * shell and shell flags.
 * Service messages are intentionally added to make teamcity messages more informative.
 */
public class CabalRunner {
    final CabalRunnerParameters parameters;

    CabalRunner(CabalRunnerParameters parameters) {
        this.parameters = parameters;
    }

    public String getProgramCommandLine(String cabalPath, OperatingSystemSpecifics os) {
        List<String> cmds = new ArrayList<String>();

        if (parameters.cleanBefore) {
            surroundWithBeginAndEnd(cmds, new CleanBlock(), cleanCommand(cabalPath));
        }

        surroundWithBeginAndEnd(cmds, new ConfigureBlock(), configureCommand(cabalPath, PluginDefaults.DEFAULT_CONFIGURE_VERBOSITY, parameters.runTests));
        surroundWithBeginAndEnd(cmds, new BuildBlock(), buildCommand(cabalPath));

        if (parameters.runTests) {
            // Decide on test options.
            TestOptions testOpts = null;
            if (parameters.testReporting.equals(TEST_REPORTING_USER)) {
                testOpts = new DefaultTestOptions();
            } else if (parameters.testReporting.equals(TEST_REPORTING_TEST_FRAMEWORK)) {
                testOpts = new TestFrameworkTestOptions();
            }
            cmds.add(testCommand(testOpts, cabalPath));
        }

        return StringUtils.join(cmds, " " + os.getConjunction() + " ");
    }

    private void surroundWithBeginAndEnd(List<String> commands, Block block, String cmd) {
        commands.add(echoCommand("\n"));
        commands.add(echoCommand(block.beginLine()));
        commands.add(cmd);
        commands.add(echoCommand(block.endLine()));
    }

    private String echoCommand(String message) {
        return "echo " + "\"" + message + "\"";
    }

    private String cleanCommand(String cabalPath) {
        return cabalPath + " clean";
    }

    private String configureCommand(String cabalPath, int verbosity, boolean runTests) {
        return cabalPath + " configure -v" + verbosity + (runTests ? " --enable-tests" : "");
    }

    private String buildCommand(String cabalPath) {
        List<String> tokens = new ArrayList();
        tokens.add(cabalPath);
        tokens.add("build");
        tokens.addAll(getBuildArgs());
        return StringUtils.join(tokens, " ");
    }

    private String testCommand(TestOptions testOpts, String cabalPath) {
        String opts;
        String testOptionsLine = (opts = testOpts.testOptions())
                .isEmpty() ? "" : String.format(" --test-options=\"%s\"", opts);
        return cabalPath + " test --show-details=always" + testOptionsLine;
    }

    /**
     * @return get arguments for executable
     */
    @NotNull
    private List<String> getBuildArgs() {
        List<String> args = new ArrayList<String>();

        // For the sakes of consistency and clarity in the build process, use all flags.
        args.add(String.format("-v%s", parameters.buildLogVerbosity.ordinal()));
        args.add(String.format("--builddir=%s", parameters.buildOutputDirectory));

        // Specify the compiler.
        args.add(String.format("--with-%s=%s", parameters.compilerName, parameters.compilerPath));

        if (!parameters.compilerFlags.isEmpty()) {
            args.add(String.format("--%s-options=%s", parameters.compilerName, parameters.compilerFlags));
        }
        return args;
    }
}


