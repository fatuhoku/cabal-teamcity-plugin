package cabalRunner.agent.log.block;

import jetbrains.buildServer.agent.BuildProgressLogger;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jetbrains.buildServer.messages.DefaultMessagesInfo.createBlockEnd;
import static jetbrains.buildServer.messages.DefaultMessagesInfo.createBlockStart;

/**
 * Blocks permit various levels of customisation when it comes to defining the
 * begin and end lines of the block:
 *
 *  - override begin/endPattern (destroy link between beginLine. coder is responsible)
 *  - override begin/endLine (use a different regex pattern. lineToPattern must behave)
 *  - override matchesBegin/matchesEnd (use non-regex for matches)
 *
 * begin/endPattern depends on the begin / end lines for consistency.
 * This consistency can and should be verified by unit tests.
 */
public abstract class Block {
    protected String defaultName; // This should uniquely identify the block.
    public boolean shouldConsumeBegin;
    public boolean shouldConsumeEnd;

    /** This is the TeamCity messages block type.
     * Refer to BLOCK_TYPE_* constants in DefaultMessagesInfo
     * In case of non-standard block type use prefix "CUSTOM_"...
     */
    protected abstract String getBlockType();

    /**
     * Produces a matched block.
     * @return
     */
    public MatchedBlock match(Matcher matcher, String indent) {
        // System.out.println(String.format("Pushing { type=\"%s\", name=\"%s\", indent=\"%s\" }", this.getClass().toString(), capturedName(matcher), indent));
        return new MatchedBlock(this, capturedName(matcher), indent);
    }

    /**
     * Returns the indent that will be applied to the children of this block.
     * @return
     */
    public String blockIndentation() { return ""; }

    public Block(String defaultName) {
        this.defaultName = defaultName;
        this.shouldConsumeBegin = true;
        this.shouldConsumeEnd = true;
    }

    /**
     * Default pattern to match for the begin line.
     * @return
     */
    public Pattern beginPattern(String indent){
        return Pattern.compile(indent+lineToPattern(beginLine()));
    }

    /**
     * Default pattern to match for the end line.
     * @return
     */
    public Pattern endPattern(String indent){
        return Pattern.compile(indent+lineToPattern(endLine()));
    }

    /**
     * TODO Remove the concept of beginLine
     * Default begin line pattern for this block
     * @return
     */
    public String beginLine() {
        return "##cabalrunner: begin["+ defaultName +"]";
    }

    /**
     * TODO Remove the concept of beginLine
     * Default end line pattern for this block
     * @return
     */
    public String endLine() {
        return "##cabalrunner: end["+ defaultName +"]";
    }

    public static String lineToPattern(String line) {
        return line.replace("[","\\[").replace("]","\\]");
    }

    /**
     * Use the default matcher (constructed out of getting the beginning pattern
     * from the beginning line).
     *
     * TODO Return some match information, such as the capture groups etc.
     * Can simply return the matcher, surely?!
     *
     *
     * @param indent
     * @param message
     * @return
     */
    public Matcher beginMatcher(String indent, String message) {
        return beginPattern(indent).matcher(message);
    }

    public Matcher endMatcher(String indent, String message) {
        return endPattern(indent).matcher(message);
    }

    /**
     * Invoked on at the beginning of a new log output block.
     * This default implementation marks the block
     * as an activity.
     * @param properties
     * @param logger
     */
    public void onBlockBegin(Matcher matcher, Map<String, String> properties, BuildProgressLogger logger){
        logger.logMessage(createBlockStart(capturedName(matcher), getBlockType()));
    }

    /**
     * The matcher could be used if needs be. However, the default is to use the block's recorded name.
     * @param block
     * @param matcher
     * @param properties
     * @param logger
     */
    public void onBlockEnd(MatchedBlock block, Matcher matcher, Map<String, String> properties, BuildProgressLogger logger) {
        logger.logMessage(createBlockEnd(block.name, getBlockType()));
    }

    /**
     * Given a matcher, extracts the name of this block from the 'begin' line
     * matcher's capture groups. Default behaviour ignores the matcher.
     */
    public String capturedName(Matcher matcher) {
        return defaultName;
    }
}
