package cabalRunner.agent.log;

import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.runner.ProcessListener;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * This handle listener does nothing but relay on messages from the process onto the build handle
 * and therefore provides no additional structure onto the build handle.
 */
public class IdentityLogListener implements ProcessListener {
    private BuildProgressLogger logger;

    public IdentityLogListener(BuildProgressLogger logger) {
        this.logger = logger;
    }

    @Override
    public void onStandardOutput(@NotNull String s) {
        logger.logMessage(DefaultMessagesInfo.createTextMessage(s));
    }

    @Override
    public void onErrorOutput(@NotNull String s) {
        logger.logMessage(DefaultMessagesInfo.createTextMessage(s));
    }

    @Override
    public void processStarted(@NotNull String s, @NotNull File file) {
        // DO NOTHING
    }

    @Override
    public void processFinished(int i) {
        // DO NOTHING
    }
}
