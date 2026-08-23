package org.mcupdater.infohud.minecolonies.events;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingWareHouse;
import com.minecolonies.core.tileentities.TileEntityWareHouse;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.mcupdater.infohud.events.ServerMonitor;
import org.mcupdater.infohud.minecolonies.config.MCConfig;
import org.mcupdater.infohud.minecolonies.records.ColonyIdentifier;
import org.mcupdater.infohud.minecolonies.InfoHUDMC;
import org.mcupdater.infohud.minecolonies.network.RaidMonitor;
import org.mcupdater.infohud.minecolonies.network.WarehouseMonitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ColonyMonitor {
	public static ColonyMonitor INSTANCE = new ColonyMonitor();
	private final Map<ColonyIdentifier,Boolean> incomingRaidMap;
	private final Map<ColonyIdentifier,Map<Integer,Integer>> freeWarehouseSlots;
	private int tickCount;

	public ColonyMonitor() {
		incomingRaidMap = new HashMap<>();
		freeWarehouseSlots = new HashMap<>();
	}

	@SubscribeEvent
	public void serverTick(ServerTickEvent.Post event) {
		tickCount++;
		if (tickCount >= MCConfig.TICK_RATE.get()) {
			tickCount = 0;
			AtomicBoolean raidChanged = new AtomicBoolean(false);
			AtomicBoolean warehouseChanged = new AtomicBoolean(false);
			IColonyManager colonyManager = MinecoloniesAPIProxy.getInstance().getColonyManager();
			colonyManager.getAllColonies().stream().forEach(colony -> {
				ColonyIdentifier identifier = new ColonyIdentifier(colony.getWorld().dimension().location(), colony.getID());
				boolean willRaid = colony.getRaiderManager().willRaidTonight();
				if (INSTANCE.getIncomingRaidMap().getOrDefault(identifier, !willRaid) != willRaid) {
					raidChanged.set(true);
					INSTANCE.getIncomingRaidMap().put(identifier, willRaid);
				}
				List<IBuilding> warehouses = colony.getServerBuildingManager().getBuildings().values().stream().filter(building -> building instanceof BuildingWareHouse).toList();
				for (int index = 0; index < warehouses.size(); index++) {
					if (event.getServer().getLevel(colony.getWorld().dimension()).getBlockEntity(warehouses.get(index).getID()) instanceof TileEntityWareHouse warehouse) {
						var cap = warehouse.getItemHandlerCap();
						if (cap != null) {
							int freeSlots = 0;
							int slots = cap.getSlots();
							for (int slot = 0; slot < slots; slot++) {
								if (cap.getStackInSlot(slot).isEmpty()) {
									freeSlots++;
								}
							}
							Map<Integer, Integer> tempEntry = INSTANCE.getFreeWarehouseSlots().getOrDefault(identifier, new HashMap<>());
							if (tempEntry.isEmpty() || tempEntry.getOrDefault(index, 0) != freeSlots) {
								warehouseChanged.set(true);
								tempEntry.put(index, freeSlots);
								INSTANCE.getFreeWarehouseSlots().put(identifier, tempEntry);
							}
						}
					}
				}
			});
			if (raidChanged.get()) {
				for (ServerPlayer player : ServerMonitor.INSTANCE.playerSet) {
					if (player.connection.hasChannel(RaidMonitor.LOCATION)) {
						PacketDistributor.sendToPlayer(player, new RaidMonitor(Map.copyOf(INSTANCE.getIncomingRaidMap())));
					}
				}
			}
			if (warehouseChanged.get()) {
				for (ServerPlayer player : ServerMonitor.INSTANCE.playerSet) {
					if (player.connection.hasChannel(WarehouseMonitor.LOCATION)) {
						PacketDistributor.sendToPlayer(player, new WarehouseMonitor(Map.copyOf(INSTANCE.getFreeWarehouseSlots())));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerConnected(PlayerEvent.PlayerLoggedInEvent event) {
		if (((ServerPlayer) event.getEntity()).connection.hasChannel(RaidMonitor.LOCATION)) {
			PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new RaidMonitor(Map.copyOf(INSTANCE.getIncomingRaidMap())));
			PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new WarehouseMonitor(Map.copyOf(INSTANCE.getFreeWarehouseSlots())));
		}
	}

	public Map<ColonyIdentifier, Boolean> getIncomingRaidMap() {
		return incomingRaidMap;
	}

	public Map<ColonyIdentifier, Map<Integer, Integer>> getFreeWarehouseSlots() {
		return freeWarehouseSlots;
	}
}
