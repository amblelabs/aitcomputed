package mc.duzo.aitcompute;

import dan200.computercraft.api.turtle.*;
import mc.duzo.aitcompute.common.upgrade.TurtleVortex;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Register {
	public static void initialize() {
		TurtleUpgrades.initialize();
	}

	public static <V, T extends V> T register(Registry<V> registry, String name, T entry) {
		return Registry.register(registry, new Identifier(ComputedMod.MOD_ID, name), entry);
	}

	public static <T extends Block> T registerBlockAndItem(String name, T entry) {
		T output = Register.register(Registries.BLOCK, name, entry);
		Registry.register(Registries.ITEM, new Identifier(ComputedMod.MOD_ID, name), new BlockItem(output, new FabricItemSettings()));
		return output;
	}

	public static class TurtleUpgrades {
		public static final Registry<TurtleUpgradeSerialiser<?>> SERIALIZERS = (Registry<TurtleUpgradeSerialiser<?>>) Registries.REGISTRIES.get(TurtleUpgradeSerialiser.registryId().getValue());

		public static final TurtleUpgradeSerialiser<TurtleVortex> VORTEX =
				registerSerializer("vortex_upgrade", TurtleUpgradeSerialiser.simpleWithCustomItem(TurtleVortex::new));

		public static <T extends TurtleUpgradeSerialiser<?>> T registerSerializer(String name, T entry) {
			return register(SERIALIZERS, name, entry);
		}

		public static void initialize() { //

		}
	}
}
