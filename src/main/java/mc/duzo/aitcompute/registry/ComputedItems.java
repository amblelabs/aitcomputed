package mc.duzo.aitcompute.registry;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import dev.amble.ait.core.AITItemGroups;
import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.item.AItem;
import dev.amble.lib.item.AItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import static mc.duzo.aitcompute.ComputedMod.MOD_ID;

public class ComputedItems extends ItemContainer {

    public static final Item VORTEX_UPGRADE = new Item(new AItemSettings().group(AITItemGroups.MAIN).maxCount(1));
}