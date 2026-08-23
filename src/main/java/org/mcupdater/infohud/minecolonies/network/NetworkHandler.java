package org.mcupdater.infohud.minecolonies.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.mcupdater.infohud.minecolonies.InfoHUDMC;

@EventBusSubscriber(modid = InfoHUDMC.MODID)
public class NetworkHandler {

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1").optional();
		registrar.playToClient(
				RaidMonitor.TYPE,
				RaidMonitor.STREAM_CODEC,
				ClientHandlersMC::raidMonitorHandler
		);
		registrar.playToClient(
				WarehouseMonitor.TYPE,
				WarehouseMonitor.STREAM_CODEC,
				ClientHandlersMC::warehouseMonitorHandler
		);
	}
}
