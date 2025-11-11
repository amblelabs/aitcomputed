package mc.duzo.aitcompute.common.peripheral;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import dev.amble.ait.api.tardis.KeyedTardisComponent;
import dev.amble.ait.api.tardis.TardisComponent;
import dev.amble.ait.api.tardis.link.v2.Linkable;
import dev.amble.ait.api.tardis.link.v2.TardisRef;
import dev.amble.ait.core.item.KeyItem;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.manager.ServerTardisManager;
import dev.amble.ait.core.util.WorldUtil;
import dev.amble.ait.data.properties.Value;
import dev.amble.ait.registry.impl.TardisComponentRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class VortexPeripheral implements IPeripheral {
	private final ITurtleAccess turtle;
	private final boolean advanced;

	// Fixed constructor
	public VortexPeripheral(boolean advanced, ITurtleAccess turtle) {
		this.advanced = advanced;
		this.turtle = turtle;
	}

	private boolean hasKey(int slot, UUID tardisId) {
		ItemStack stack = this.turtle.getInventory().getStack(slot);

		if (!(stack.getItem() instanceof KeyItem key))
			return false;

		if (key.hasProtocol(KeyItem.Protocols.SKELETON))
			return true;

		Tardis found = KeyItem.getTardisStatic(this.turtle.getLevel(), stack);
		return found != null && tardisId.equals(found.getUuid());
	}

	private Tardis getTardis(UUID tardis) {
		return ServerTardisManager.getInstance().demandTardis(this.turtle.getLevel().getServer(), tardis);
	}

	/**
	 * Finds tardis UUID from the current position of a turtle
	 * turtle must be placed inside the interior of a tardis to return a valid result
	 * @return string uuid, or empty if invalid
	 */
	@LuaFunction
	public final String findTardisId(Linkable linkable) {
		if (linkable != null && linkable.isLinked()) {
			TardisRef ref = linkable.tardis();
			if (ref != null && ref.isPresent()) {
				// Return the UUID of the linked Tardis
				return ref.get().getUuid().toString();
			}
		}
		return "";
	}

	/**
	 * Sets a property on a tardis
	 * @param args tardis: str, component: str, value: str, data: str
	 * @return success
	 */
	@LuaFunction
	public final <T> boolean set(IArguments args) throws LuaException {
		UUID tardisId = UUID.fromString(args.getString(0));
		int slot = this.turtle.getSelectedSlot();
		if (!hasKey(slot, tardisId)) return false;

		Tardis tardis = getTardis(tardisId); // poo
		TardisComponent.IdLike id = TardisComponentRegistry.getInstance().get(args.getString(1).toUpperCase());

		if (!(tardis.handler(id) instanceof KeyedTardisComponent keyed)) return false;

		String valueName = args.getString(2);
		Value<T> value = keyed.getPropertyData().getExact(valueName);
		Class<?> classOfT = value.getProperty().getType().getClazz();

		T obj = (T) ServerTardisManager.getInstance().getFileGson().fromJson(args.getString(3), classOfT);

		value.set(obj);

		return true;
	}

	/**
	 * Gets a property of a tardis
	 * @param args tardis: str, component: str, value: str
	 * @return the found property or empty string
	 */
	@LuaFunction
	public final <T> String get(IArguments args) throws LuaException {
		UUID tardisId = UUID.fromString(args.getString(0));
		int slot = this.turtle.getSelectedSlot();
		if (!hasKey(slot, tardisId)) return "No Key Found";

		Tardis tardis = getTardis(tardisId); // poo
		TardisComponent.IdLike id = TardisComponentRegistry.getInstance().get(args.getString(1).toUpperCase());

		if (!(tardis.handler(id) instanceof KeyedTardisComponent keyed)) return "";

		String valueName = args.getString(2);
		Value<T> value = keyed.getPropertyData().getExact(valueName);
		T obj = value.get();

		String json = ServerTardisManager.getInstance().getFileGson().toJson(obj);

		return json;
	}

	@LuaFunction
	public final <T> String openDoor(IArguments args) throws LuaException {
		UUID tardisId = UUID.fromString(args.getString(0));
		int slot = this.turtle.getSelectedSlot();
		if (!hasKey(slot, tardisId)) return "No Key Found";

		Tardis tardis = getTardis(tardisId); // poo

		tardis.door().openDoors();

        return "Doors Opened";
    }

	@LuaFunction
	public final <T> String closeDoor(IArguments args) throws LuaException {
		UUID tardisId = UUID.fromString(args.getString(0));
		int slot = this.turtle.getSelectedSlot();
		if (!hasKey(slot, tardisId)) return "No Key Found";

		Tardis tardis = getTardis(tardisId); // poo

		tardis.door().closeDoors();

		return "Doors Closed";
	}

	@LuaFunction
	public final <T> String lockDoor(IArguments args) throws LuaException {
		UUID tardisId = UUID.fromString(args.getString(0));
		int slot = this.turtle.getSelectedSlot();
		if (!hasKey(slot, tardisId)) return "No Key Found";

		Tardis tardis = getTardis(tardisId); // poo

		tardis.door().setLocked(true);

		return "Doors Locked";
	}

	@LuaFunction
	public final <T> String unlockDoor(IArguments args) throws LuaException {
		UUID tardisId = UUID.fromString(args.getString(0));
		int slot = this.turtle.getSelectedSlot();
		if (!hasKey(slot, tardisId)) return "No Key Found";

		Tardis tardis = getTardis(tardisId); // poo

		tardis.door().setLocked(false);
		return "Doors Unlocked";
	}

	/**
	 * sets destination of a tardis
	 * @param args uuid: str, x: int, y: int, z: int, rotation: str, dimension: str
	 */

	@LuaFunction
	public final <T> String setDestination(IArguments args) throws LuaException {
		UUID tardisId = UUID.fromString(args.getString(0));
		int slot = this.turtle.getSelectedSlot();
		if (!hasKey(slot, tardisId)) return "No Key Found";

		Tardis tardis = getTardis(tardisId); // poo

		// grab coords
		int x, y, z;
		try {
			x = args.getInt(1);
			y = args.getInt(2);
			z = args.getInt(3);
		} catch (NumberFormatException e) {
			throw new LuaException("Coordinates must be valid integers");
		}

		BlockPos destination = new BlockPos(x, y, z);

		// grab rotation
		byte rotation = parseDirection(args.getString(4));

		//grab dimensions
		String dimId = args.getString(5);
		List<ServerWorld> dims = WorldUtil.getTravelWorlds();
		ServerWorld destWorld = dims.stream().filter(world -> world.getRegistryKey().getValue().toString().equals(dimId)).findFirst().orElse(null);

        if (destWorld == null) {
			throw new LuaException("Invalid or locked dimension: " + dimId);
		}

		tardis.travel().destination(cached ->
				cached.world(destWorld).pos(destination).rotation(rotation)
		);

		return "Destination set to " + dimId + " @ " + destination.toShortString() + " | rotation: " + rotation;
	}

	/**
	 * enables autopilot and attempts a takeoff
	 * @param args uuid: str
	 */
	@LuaFunction
	public final void startStableFlight(IArguments args) throws LuaException {
		UUID tardisId = UUID.fromString(args.getString(0));
		int slot = this.turtle.getSelectedSlot();
		if (!hasKey(slot, tardisId)) return;

		Tardis tardis = getTardis(tardisId);
		tardis.travel().autopilot(true);
		tardis.travel().dematerialize();
	}

	@Override
	public String getType() {
		return "aitcomputed:vortex_upgrade";
	}

	@Override
	public boolean equals(@Nullable IPeripheral other) {
		if (other == null) return false;
		return other.getType().equals(this.getType());
	}

	private static byte parseDirection(String dir) throws LuaException {
		return switch (dir.toLowerCase()) {
			case "north" -> 0;
			case "north east", "northeast" -> 1;
			case "east" -> 2;
			case "south east", "southeast" -> 3;
			case "south" -> 4;
			case "south west", "southwest" -> 5;
			case "west" -> 6;
			case "north west", "northwest" -> 7;
			default -> throw new LuaException("Invalid direction: " + dir
					+ ". Expected one of north, north east, east, south east, south, south west, west, north west");
		};
	}
}
