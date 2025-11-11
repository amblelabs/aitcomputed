package mc.duzo.aitcompute;

import dev.amble.lib.container.RegistryContainer;
import mc.duzo.aitcompute.common.component.VortexCommunicator;
import mc.duzo.aitcompute.registry.ComputedBlockEntityTypes;
import mc.duzo.aitcompute.registry.ComputedBlocks;
import mc.duzo.aitcompute.registry.ComputedItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ComputedMod implements ModInitializer {
	public static final String MOD_ID = "aitcompute";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static MinecraftServer SERVER;

	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		RegistryContainer.register(ComputedBlockEntityTypes.class, MOD_ID);
		RegistryContainer.register(ComputedItems.class, MOD_ID);
		RegistryContainer.register(ComputedBlocks.class, MOD_ID);
		Register.initialize();
		registerEvents();

		VortexCommunicator.register();
	}

	private void registerEvents() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> SERVER = server);
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> SERVER = null);
		ServerWorldEvents.UNLOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD) {
				SERVER = null;
			}
		});

		ServerWorldEvents.LOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD) {
				SERVER = server;
			}
		});
	}

	public static Identifier id(String s) {
		return new Identifier(MOD_ID, s);
	}

	public static Optional<MinecraftServer> getServer() {
		return Optional.ofNullable(SERVER);
	}
}
