package mc.duzo.aitcompute.registry;

import dev.amble.lib.animation.HasBedrockModel;
import dev.amble.lib.container.impl.BlockEntityContainer;
import mc.duzo.aitcompute.registry.blockentities.VortexCommunicatorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class ComputedBlockEntityTypes implements BlockEntityContainer {
    @HasBedrockModel
    public static BlockEntityType<VortexCommunicatorBlockEntity> TEST_BLOCK = FabricBlockEntityTypeBuilder.create(VortexCommunicatorBlockEntity::new, ComputedBlocks.VORTEX_COMMUNICATOR).build();
}