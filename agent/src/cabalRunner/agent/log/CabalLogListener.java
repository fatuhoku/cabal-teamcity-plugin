package cabalRunner.agent.log;

import cabalRunner.agent.log.block.Block;
import cabalRunner.agent.log.block.CabalBlocksList;
import cabalRunner.agent.log.block.MatchedBlock;
import cabalRunner.agent.log.line.CabalLogLines;
import cabalRunner.agent.log.line.Line;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.runner.ProcessListener;
import org.jetbrains.annotations.NotNull;
import cabalRunner.common.PluginDefaults;

import java.io.File;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;

/**
 * Listens to progress, and logs to provide build progress by matching logged messages to known events.
 * The alternative is to have Cabal produce TeamCity service messages, which simply is not going to happen.
 * Provide nested 'block' structure to handle blocks hierarchically.
 *
 * Agent configuration is provided to enrich the build output.
 * Four kinds of loggable things:
 * - activity(defaultName,[desc],type)    -- activities corresponds to a Block.
 * - target()
 * - progress()
 * <p/>
 * We shall not use 'target', as none of the things we've talked about are targets.
 * Progress are meant for long running tasks that have a single defaultName; this is not suitable.
 * We shall just use activity. No Ant tasks are invoked
 * <p/>
 * Configure...
 * create
 * <p/>
 * Build ...
 * createCompilationBlockStart()
 * createCompilationBlockEnd()
 * <p/>
 * Linking...
 * linkages...
 * Linking dist/build/get-player-coords/get-player-coords ...
 * Linking dist/build/roundtrip/roundtrip ...
 * Linking dist/build/oneblock/oneblock ...
 * Linking dist/build/minecraft-png-import/minecraft-png-import ...
 * <p/>
 * Tests:
 * - started
 * - finished
 * - ignored
 * - failed...
 * <p/>
 * Completion...
 * Right now, I just want to see a handle...
 */
public class CabalLogListener implements ProcessListener {
    private final Stack<MatchedBlock> matchedStack = new Stack<MatchedBlock>();
    private final BuildProgressLogger logger;
    private final Map<String, String> properties;

    // The handle parser is a thin wrapper around the BuildProgressLogger that
    // enhances the handle information logged.
    public CabalLogListener(BuildAgentConfiguration properties, BuildProgressLogger logger) {
        this.properties = PluginDefaults.populateBlankPropertiesWithDefaults(properties.getConfigurationParameters());
        this.logger = logger;
    }

    /**
     * Optionally trigger other messages to be produced from the build log
     * by recovering structure...
     *
     * @param line
     */
    public void handle(String line) {
        if (!(matchBlockEnd(line)     // Current matched block end or any previously matched block
           || matchBlockBegin(line)
           )) {
            handleLine(line);
        }
    }

    // The following 'matchXXX' methods' return values mean the following:
    // 'true' if the line was consumed
    // 'false' otherwise.

    /**
     * Matches the given line against the end of matched blocks, starting from the most recently matched.
     * Closure of any block earlier in the stack will close all blocks within that block,
     * in most recently matched order.
     * @param line The line that is predicated upon
     * @return
     */
    private boolean matchBlockEnd(String line) {
        Matcher matcher;

        // Iterate from the back of the stack...
        // Check if i matches the end. If it does, keep popping until the size gets to index.
        // i.e. if element 0 matched, then we pop until the stack is empty!
        for (int i = matchedStack.size()-1; i >= 0; i--) {
            MatchedBlock matched = matchedStack.get(i);
            if ((matcher = matched.block.endMatcher(matched.matchIndentation, line)).matches()) {
                while(i < matchedStack.size()) {
                    final MatchedBlock popped = matchedStack.pop();
                    popped.block.onBlockEnd(popped, matcher, properties, logger);
                }
                return matched.block.shouldConsumeEnd;
            }
        }

        return false;
    }

    /**
     * Returns true whenever the line represents the beginning of a new block.
     * @param line The line that is being predicated upon
     * @return
     */
    private boolean matchBlockBegin(String line) {
        Matcher matcher;
        for (Block b : CabalBlocksList.blocks) {
            matcher = b.beginMatcher(currentIndent(), line);
            if (matcher.matches()) {
                b.onBlockBegin(matcher, properties, logger);
                matchedStack.push(b.match(matcher, currentIndent())); // Match at no indent...
                return b.shouldConsumeBegin;
            }
        }
        return false;
    }

    /**
     * Returns the current level of indentation.
     * @return
     */
    private String currentIndent() {
        if (matchedStack.isEmpty()) {
            return "";
        } else {
            return prevIndent() + matchedStack.peek().block.blockIndentation();
        }
    }

    /**
     * Returns the previous level of indentation
     * @return
     */
    private String prevIndent() {
        if (matchedStack.isEmpty()) {
            return null;
        } else {
            return matchedStack.peek().matchIndentation;
        }
    }

    /**
     * Convenience method for logging a line, automatically deducing what kind of status line it is (error state)
     * Check against known messages such as compilation messages.
     *
     * @param message
     */
    private void handleLine(String message) {
        Matcher matcher;
        for (Line line : CabalLogLines.list) {
            if ((matcher = line.matcher(message)).matches()) {
                line.onLineMatch(matcher, logger);
                return;
            }
        }
    }

    // We define:
    // When there are no matched blocks, current indentation is "".
    // When a block is matched, the level at which it was matched (the current indentation)
    // is pushed onto the stack.
    // To compute the current indentation, ask the MatchedBlock.
    // Thus the beginning of the next level is really the beginning of the current level.
    // The _only_ legal place to begin a block is the current indentation level.
    // So check whether it's legal to begin on the current indentation level;
    // The problem with closing before
    //
    // Match current end; pop block.
    // Match current begin; push new block, possibly increasing indent

    // Ensure that if the 'end' matches, double check that it's not also

    // For every standard message, figure out whether it's alright.
    @Override
    public void onStandardOutput(@NotNull String s) {
        handle(s);
    }

    // Errors are treated as errors. But we don't know that. So we match it against a big list of error and warnings.
    @Override
    public void onErrorOutput(@NotNull String s) {
        handle(s);
    }

    @Override
    public void processStarted(@NotNull String commandLine, @NotNull File workingDir) {
    }

    @Override
    public void processFinished(int exitCode) {
    }
}
