package mc.duzo.aitcompute.common.component;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import dan200.computercraft.api.lua.LuaFunction;
import mc.duzo.aitcompute.registry.ComputedBlockEntityTypes;
import mc.duzo.aitcompute.registry.blockentities.VortexCommunicatorBlockEntity;
import org.jetbrains.annotations.Nullable;

public class VortexCommunicator {
    public static class VortexCommunicatorPeripheral implements IPeripheral {

        public VortexCommunicatorPeripheral(VortexCommunicatorBlockEntity f) {

        }

        @Override
        public String getType() {
            return "vortex_communicator";
        }

        @LuaFunction
        public String getStatus() {
            return "Vortex Communicator active!";
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
    }

    public static void register() {
        PeripheralLookup.get().registerForBlockEntity((f, s) -> new VortexCommunicatorPeripheral(f), ComputedBlockEntityTypes.VORTEX_COMMUNICATOR);
    }
}
