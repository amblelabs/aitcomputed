package mc.duzo.aitcompute.client;

import dan200.computercraft.api.client.FabricComputerCraftAPIClient;
import dan200.computercraft.api.client.turtle.RegisterTurtleUpgradeModeller;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import mc.duzo.aitcompute.ComputedMod;
import mc.duzo.aitcompute.Register;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;

public class ComputedClient implements ClientModInitializer {
	/**
	 * Runs the mod initializer on the client environment.
	 */
	@Override
	public void onInitializeClient() {
		registerTurtleModellers(FabricComputerCraftAPIClient::registerTurtleUpgradeModeller);
	}

	private static void registerTurtleModellers(RegisterTurtleUpgradeModeller register) {
		register.register(Register.TurtleUpgrades.VORTEX, TurtleUpgradeModeller.sided(
				new Identifier(ComputedMod.MOD_ID, "block/turtle_vortex_left"),
				new Identifier(ComputedMod.MOD_ID, "block/turtle_vortex_right")
		));
	}

}
