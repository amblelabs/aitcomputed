package mc.duzo.aitcompute.common.component;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import dan200.computercraft.api.lua.LuaFunction;
import dev.amble.ait.api.tardis.KeyedTardisComponent;
import dev.amble.ait.api.tardis.TardisComponent;
import dev.amble.ait.core.AITSounds;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.manager.ServerTardisManager;
import dev.amble.ait.core.util.WorldUtil;
import dev.amble.ait.data.properties.Value;
import dev.amble.ait.registry.impl.TardisComponentRegistry;
import mc.duzo.aitcompute.registry.ComputedBlockEntityTypes;
import mc.duzo.aitcompute.registry.blockentities.VortexCommunicatorBlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class VortexCommunicator {
    public static class VortexCommunicatorPeripheral implements IPeripheral {
        private final VortexCommunicatorBlockEntity blockEntity;

        public VortexCommunicatorPeripheral(VortexCommunicatorBlockEntity f) {
            this.blockEntity = f;
        }

        private Tardis getTardis(UUID tardis) {
            if (blockEntity == null) return null;
            MinecraftServer server = blockEntity.getWorld().getServer();
            return ServerTardisManager.getInstance().demandTardis(server, tardis);
        }

        @Override
        public String getType() {
            return "vortex_communicator";
        }

        /**
         * Sets a property on a tardis
         * @param args tardis: str, component: str, value: str, data: str
         * @return success
         */
        @LuaFunction
        public final <T> boolean set(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId); // poo
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

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

            Tardis tardis = getTardis(tardisId); // poo
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

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

            Tardis tardis = getTardis(tardisId); // poo
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            tardis.door().openDoors();

            return "Doors Opened";
        }

        @LuaFunction
        public final <T> String closeDoor(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId); // poo
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            tardis.door().closeDoors();

            return "Doors Closed";
        }

        @LuaFunction
        public final <T> String lockDoor(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId); // poo
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            tardis.door().setLocked(true);

            return "Doors Locked";
        }

        @LuaFunction
        public final <T> String unlockDoor(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId); // poo
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

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

            Tardis tardis = getTardis(tardisId); // poo
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

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

        @LuaFunction
        public final double getX(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            BlockPos pos = tardis.travel().destination().getPos();
            return pos.getX();
        }

        @LuaFunction
        public final double getY(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            BlockPos pos = tardis.travel().destination().getPos();
            return pos.getY();
        }

        @LuaFunction
        public final double getZ(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            BlockPos pos = tardis.travel().destination().getPos();
            return pos.getZ();
        }

        @LuaFunction
        public final String getDimension(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            ServerWorld world = tardis.travel().destination().getWorld();

            return world.getRegistryKey().getValue().toString();
        }

        @LuaFunction
        public final String getRotation(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            byte rotation = tardis.travel().destination().getRotation();

            return switch (rotation) {
                case 0 -> "north";
                case 1 -> "north east";
                case 2 -> "east";
                case 3 -> "south east";
                case 4 -> "south";
                case 5 -> "south west";
                case 6 -> "west";
                case 7 -> "north west";
                default -> "unknown";
            };
        }

        /**
         * enables autopilot and attempts a takeoff
         * @param args uuid: str
         */
        @LuaFunction
        public final void startStableFlight(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            tardis.travel().autopilot(true);
            tardis.travel().dematerialize();
        }

        @LuaFunction
        public final void landTardis(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            tardis.travel().rematerialize();
        }

        @LuaFunction
        public final boolean isInFlight(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            return !tardis.travel().isLanded();
        }

        @LuaFunction
        public final boolean isDoorOpen(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            return tardis.door().isOpen();
        }

        @LuaFunction
        public final boolean isDoorLocked(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            return tardis.door().locked();
        }

        @LuaFunction
        public final double getFuelLevel(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            return tardis.fuel().getCurrentFuel();
        }

        @LuaFunction
        public final double getMaxFuelCapacity(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            return tardis.fuel().getMaxFuel();
        }

        @LuaFunction
        public final boolean setRefuel(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            tardis.setRefueling(args.getBoolean(1));

            return args.getBoolean(1);
        }

        @LuaFunction
        public final void randomiseCoords(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");
            BlockPos pos = new BlockPos(
                    (int) (Math.random() * 40001 - 20000),
                    64,
                    (int) (Math.random() * 40001 - 20000)
            );

            tardis.travel().destination().pos(pos);
        }

        @LuaFunction
        public final void fastReturn(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            BlockPos previous = tardis.travel().previousPosition().getPos();
            if (previous == null) throw new LuaException("No previous position recorded.");

            tardis.travel().destination().pos(previous);
        }

        @LuaFunction
        public final boolean isPowered(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            return tardis.fuel().hasPower();
        }

        @LuaFunction
        public final void togglePower(IArguments args) throws LuaException {
            UUID tardisId = UUID.fromString(args.getString(0));

            Tardis tardis = getTardis(tardisId);
            if (tardis == null) throw new LuaException("Invalid TARDIS UUID.");

            tardis.fuel().togglePower();
            blockEntity.getWorld().playSound(null, blockEntity.getPos(), AITSounds.MAD_MAN_MUSIC, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }

        @Override
        public void attach(dan200.computercraft.api.peripheral.IComputerAccess computer) {
            // Called when a computer attaches
        }

        @Override
        public void detach(dan200.computercraft.api.peripheral.IComputerAccess computer) {
            // Called when a computer detaches
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return other instanceof VortexCommunicatorPeripheral;
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

    public static void register() {
        PeripheralLookup.get().registerForBlockEntity((f, s) -> new VortexCommunicatorPeripheral(f), ComputedBlockEntityTypes.VORTEX_COMMUNICATOR);
    }
}
