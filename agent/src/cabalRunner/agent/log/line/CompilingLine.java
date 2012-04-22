package cabalRunner.agent.log.line;

import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import jetbrains.buildServer.messages.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cabalRunner.agent.log.block.LogConstants.PATTERN_NAT_NUM;
import static cabalRunner.agent.log.block.LogConstants.PATTERN_REL_PATH;

/**
 * [created by: H.Poon on: 09/04/2012 at: 22:29]
 */
public class CompilingLine extends Line {

    @Override
    public Status getStatus() {
        return Status.NORMAL;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile(String.format("\\[ (%1$s) of (%1$s)\\] Compiling ([A-Z]\\w*)\\s*\\( (%2$s), (%2$s) \\)",
                PATTERN_NAT_NUM, PATTERN_REL_PATH));
    }

    @Override
    public void onLineMatch(Matcher matcher, BuildProgressLogger logger) {
        logger.logMessage(DefaultMessagesInfo.createTextMessage(
                String.format("Compiling %s ...", matcher.group(4))));
    }
}
