package cabalRunner.agent.log.line;

import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.messages.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Line {

    public abstract Status getStatus();

    public abstract Pattern getPattern();

    public abstract void onLineMatch(Matcher matcher, BuildProgressLogger logger);

    public Matcher matcher(String message) {
        return getPattern().matcher(message);
    }
}
