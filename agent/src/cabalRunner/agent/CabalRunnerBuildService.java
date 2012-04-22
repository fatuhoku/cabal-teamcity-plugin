package cabalRunner.agent;

import cabalRunner.agent.os.WindowsOperatingSystemSpecifics;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.BuildAgentSystemInfo;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProcessListener;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.TerminationAction;
import org.jetbrains.annotations.NotNull;
import cabalRunner.agent.log.CabalLogListener;
import cabalRunner.agent.os.OperatingSystemSpecifics;
import cabalRunner.agent.os.UnixOperatingSystemSpecifics;
import cabalRunner.common.Parameters;
import cabalRunner.common.PluginConstants;
import cabalRunner.common.PluginDefaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class manages the lifecycle of the agent.
 * TODO consider overriding and handling some of the other methods provided by the adapter.
 */
public class CabalRunnerBuildService extends BuildServiceAdapter {
    private CabalRunner runner;
    private OperatingSystemSpecifics os;

    public CabalRunnerBuildService() {
    }

    /**
     * Ensure that our trusty IdentityLogListener is registered.
     * @return
     */
    @NotNull
    @Override
    public List<ProcessListener> getListeners() {
        List<ProcessListener> existingListeners = super.getListeners();
        List<ProcessListener> listeners = new ArrayList();
        listeners.addAll(existingListeners);
        listeners.add(new CabalLogListener(getAgentConfiguration(), getLogger()));
        return listeners;
    }

    @Override
    public void afterInitialized() {
        runner = new CabalRunner(runnerParameters());
        BuildAgentSystemInfo info = getAgentConfiguration().getSystemInfo();
        if (info.isWindows()) {
            os = new WindowsOperatingSystemSpecifics();
        } else { // Unix and Mac
            os = new UnixOperatingSystemSpecifics();
        }
    }

    // The actual runner parameters that should be used.
    private CabalRunnerParameters runnerParameters() {
        return new CabalRunnerParameters(
                PluginDefaults.populateBlankPropertiesWithDefaults(getRunnerParameters())
        );
    }

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        // Windows:     cmd /C "cabal configure; cabal build; cabal test"
        // Mac & Unix:  sh -c  "cabal configure; cabal build; cabal test"

        // At this point, we can choose between two
        // - Agent Cabal discovery
        // - Explicitly provided Cabal path
        // If we use the absolute path of the cabal executable there are no excuses.
        String cabalPath = Parameters.getString(getConfigParameters(), PluginConstants.AGENT_CONFIGURATION_CABAL_PATH);

        return createProgramCommandline(os.getShell(),
                Arrays.asList(
                        os.getShellArgumentCommandFlag(),
                        runner.getProgramCommandLine(cabalPath, os)
                )
        );
    }


    @Override
    @NotNull
    public TerminationAction interrupt() {
        return super.interrupt();
    }
}
