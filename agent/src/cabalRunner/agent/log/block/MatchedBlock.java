package cabalRunner.agent.log.block;

/**
 * A match of a block.
 * Manages
 *  - The indentation at which the begin line of the block is made
 *  - Convenience methods for beginning and closing off a new matched block (calls into Block)
 *
 *
 */
public class MatchedBlock {
    
    public final Block block;
    public final String name;
    public final String matchIndentation; // the indentation level at which the match was made

    MatchedBlock(Block block, String name, String indent)
    {
        this.block = block;
        this.name = name;
        this.matchIndentation = indent;
    }

}
