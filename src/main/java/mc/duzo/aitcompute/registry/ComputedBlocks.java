package mc.duzo.aitcompute.registry;

import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.container.impl.BlockContainer;
import mc.duzo.aitcompute.registry.blocks.VortexCommunicatorBlock;
import net.minecraft.block.Block;

public class ComputedBlocks extends BlockContainer {
    public static final Block VORTEX_COMMUNICATOR = new VortexCommunicatorBlock(ABlockSettings.create().burnable());
}
