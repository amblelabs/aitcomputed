package mc.duzo.aitcompute.registry.blockentities;

import dev.amble.lib.blockentity.ABlockEntity;
import mc.duzo.aitcompute.ComputedMod;
import mc.duzo.aitcompute.registry.ComputedBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VortexCommunicatorBlockEntity extends ABlockEntity {

    public VortexCommunicatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public VortexCommunicatorBlockEntity(BlockPos pos, BlockState state) {
        this(ComputedBlockEntityTypes.VORTEX_COMMUNICATOR, pos, state);
    }

    public String getModId() {
        return ComputedMod.MOD_ID;
    }


    @Override
    public void tick(World world, BlockPos pos, BlockState state) {
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        return ActionResult.FAIL;
    }

}