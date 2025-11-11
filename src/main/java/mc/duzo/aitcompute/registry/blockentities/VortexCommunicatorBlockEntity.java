package mc.duzo.aitcompute.registry.blockentities;

import dev.amble.lib.animation.AnimatedBlockEntity;
import dev.amble.lib.blockentity.ABlockEntity;
import dev.amble.lib.client.bedrock.BedrockAnimationReference;
import dev.amble.lib.client.bedrock.BedrockModelReference;
import mc.duzo.aitcompute.ComputedMod;
import mc.duzo.aitcompute.registry.ComputedBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VortexCommunicatorBlockEntity extends ABlockEntity implements AnimatedBlockEntity {
    private static final BedrockModelReference MODEL = new BedrockModelReference(ComputedMod.MOD_ID, "test_block");

    public VortexCommunicatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public VortexCommunicatorBlockEntity(BlockPos pos, BlockState state) {
        this(ComputedBlockEntityTypes.TEST_BLOCK, pos, state);
    }

    @Override
    public String getModId() {
        return ComputedMod.MOD_ID;
    }

    @Override
    public String getTexturePrefix() {
        return "block";
    }

    @Override
    public @Nullable BedrockModelReference getModel() {
        return MODEL;
    }

    @Override
    public boolean hasEmission() {
        return true;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state) {
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        this.playAnimation(new BedrockAnimationReference("test_block", "use"));
        return ActionResult.SUCCESS;
    }

    @Override
    public int getAge() {
        return 0;
    }

    @Override
    public AnimationState getAnimationState() {
        return null;
    }
}