package cabalRunner.agent.log.block;

import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import cabalRunner.common.Parameters;
import cabalRunner.common.PluginConstants;

import java.util.Map;
import java.util.regex.Matcher;

/**
 * Captures all output that occurs during a 'cabal build'.
 */
public class BuildBlock extends Block {
    public BuildBlock() {
        super("build");
    }

    @Override
    protected String getBlockType() {
        return LogConstants.BLOCK_TYPE_CABAL_BUILD;
    }

    @Override
    public void onBlockBegin(Matcher matcher, Map<String, String> properties, BuildProgressLogger logger) {
        String compilerName = Parameters.getString(properties, PluginConstants.PROPERTY_COMPILER_NAME);
        logger.logMessage(DefaultMessagesInfo.createCompilationBlockStart(compilerName));
        // TODO we won't know the project matchIndentation if we make this a progress!!
    }

    @Override
    public void onBlockEnd(MatchedBlock block, Matcher matcher, Map<String, String> properties, BuildProgressLogger logger) {
        String compilerName = Parameters.getString(properties, PluginConstants.PROPERTY_COMPILER_NAME);
        logger.logMessage(DefaultMessagesInfo.createCompilationBlockEnd(compilerName));
    }
}
