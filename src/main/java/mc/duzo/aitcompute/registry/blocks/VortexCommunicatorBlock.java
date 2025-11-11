package mc.duzo.aitcompute.registry.blocks;

import dev.amble.lib.block.ABlockSettings;
import mc.duzo.aitcompute.registry.blockentities.VortexCommunicatorBlockEntity;
import net.minecraft.block.*;
import org.jetbrains.annotations.Nullable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;


public class VortexCommunicatorBlock extends Block implements BlockEntityProvider {

    public VortexCommunicatorBlock(ABlockSettings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new VortexCommunicatorBlockEntity(pos, state);
    }
}
