package mc.duzo.aitcompute.registry.blocks;

import dev.amble.lib.block.ABlockSettings;
import mc.duzo.aitcompute.registry.blockentities.VortexCommunicatorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import static dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior.FACING;


public class VortexCommunicatorBlock extends Block implements BlockEntityProvider {

    public VortexCommunicatorBlock(ABlockSettings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection());
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new VortexCommunicatorBlockEntity(pos, state);
    }
}
