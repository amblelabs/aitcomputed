package mc.duzo.aitcompute.registry;

import dev.amble.lib.container.impl.BlockEntityContainer;
import mc.duzo.aitcompute.registry.blockentities.VortexCommunicatorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class ComputedBlockEntityTypes implements BlockEntityContainer {
    public static BlockEntityType<VortexCommunicatorBlockEntity> VORTEX_COMMUNICATOR = FabricBlockEntityTypeBuilder.create(VortexCommunicatorBlockEntity::new, ComputedBlocks.VORTEX_COMMUNICATOR).build();
}