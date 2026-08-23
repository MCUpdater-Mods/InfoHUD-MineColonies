package org.mcupdater.infohud.minecolonies;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.mcupdater.infohud.minecolonies.records.ColonyIdentifier;
import org.mcupdater.infohud.minecolonies.tags.SubTagRegistry;

import java.util.HashMap;
import java.util.Map;

@Mod(value = InfoHUDMC.MODID, dist = Dist.CLIENT)
public class InfoHUDMCClient {

	public static Map<ColonyIdentifier,Boolean> localIncomingRaidMap = new HashMap<>();
	public static Map<ColonyIdentifier,Map<Integer,Integer>> localFreeWarehouseSlots = new HashMap<>();

	public InfoHUDMCClient(IEventBus modEventBus, ModContainer modContainer) {
		SubTagRegistry.init();
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}
}
