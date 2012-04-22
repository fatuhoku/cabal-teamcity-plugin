package cabalRunner.agent.log.block;

/**
 * Captures all output that occurs during a 'cabal configure'.
 * Configuration is a block (foldable).
 */
public class ConfigureBlock extends Block {

    public ConfigureBlock() {
        super("configure");
    }

    @Override
    protected String getBlockType() {
        return LogConstants.BLOCK_TYPE_CABAL_CONFIGURE;
    }
}
