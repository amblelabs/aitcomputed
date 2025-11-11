package mc.duzo.aitcompute.registry;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import dev.amble.lib.container.impl.ItemContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import static mc.duzo.aitcompute.ComputedMod.MOD_ID;

public class ComputedItems extends ItemContainer {

    public static final ITurtleUpgrade VORTEX_PERIPHERAL = new ITurtleUpgrade() {
        @Override
        public Identifier getUpgradeID() {
            return new Identifier(MOD_ID + ":vortex_upgrade");
        }

        @Override
        public String getUnlocalisedAdjective() {
            return "TARDIS";
        }

        @Override
        public ItemStack getCraftingItem() {
            return null;
        }

        @Override
        public ItemStack getUpgradeItem(NbtCompound upgradeData) {
            return ITurtleUpgrade.super.getUpgradeItem(upgradeData);
        }

        @Override
        public NbtCompound getUpgradeData(ItemStack stack) {
            return ITurtleUpgrade.super.getUpgradeData(stack);
        }

        @Override
        public boolean isItemSuitable(ItemStack stack) {
            return ITurtleUpgrade.super.isItemSuitable(stack);
        }

        @Override
        public TurtleUpgradeType getType() {
            return null;
        }

        @Override
        public @Nullable IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
            return ITurtleUpgrade.super.createPeripheral(turtle, side);
        }

        @Override
        public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, Direction direction) {
            return ITurtleUpgrade.super.useTool(turtle, side, verb, direction);
        }

        @Override
        public void update(ITurtleAccess turtle, TurtleSide side) {
            ITurtleUpgrade.super.update(turtle, side);
        }

        @Override
        public NbtCompound getPersistedData(NbtCompound upgradeData) {
            return ITurtleUpgrade.super.getPersistedData(upgradeData);
        }
    };
}