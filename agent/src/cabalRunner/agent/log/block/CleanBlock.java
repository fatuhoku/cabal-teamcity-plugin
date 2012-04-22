package cabalRunner.agent.log.block;

/**
 * Captures all output that occurs during a 'cabal clean'.
 */
public class CleanBlock extends Block {
    public CleanBlock() {
        super("clean");
    }

    @Override
    protected String getBlockType() {
        return LogConstants.BLOCK_TYPE_CABAL_CLEAN;
    }
}
