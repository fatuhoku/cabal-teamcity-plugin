package cabalRunner.agent.log.line;

import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.messages.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [created by: H.Poon on: 09/04/2012 at: 22:43]
 * TODO Group up tests by their test group. Complete getPattern and onLineMatch() methods
 */
public class TestGroupLine extends Line {
    @Override
    public Status getStatus() {
        return Status.NORMAL;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("");
    }

    @Override
    public void onLineMatch(Matcher matcher, BuildProgressLogger logger) {
        // Do Nothing
    }
}
