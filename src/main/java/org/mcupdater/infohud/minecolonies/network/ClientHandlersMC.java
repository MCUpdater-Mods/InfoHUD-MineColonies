package org.mcupdater.infohud.minecolonies.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.mcupdater.infohud.minecolonies.InfoHUDMC;
import org.mcupdater.infohud.minecolonies.InfoHUDMCClient;

public class ClientHandlersMC {

	public static void raidMonitorHandler(final RaidMonitor raidMonitor, IPayloadContext context) {
		InfoHUDMCClient.localIncomingRaidMap.putAll(raidMonitor.incomingRaids());
	}

	public static void warehouseMonitorHandler(final WarehouseMonitor warehouseMonitor, IPayloadContext context) {
		InfoHUDMCClient.localFreeWarehouseSlots.putAll(warehouseMonitor.freeWarehouseSlots());
	}
}
