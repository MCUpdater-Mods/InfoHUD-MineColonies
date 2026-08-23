package org.mcupdater.infohud.minecolonies.tags;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyView;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.api.colony.interactionhandling.IInteractionResponseHandler;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingWareHouse;
import com.minecolonies.core.entity.ai.workers.util.BuildingProgressStage;
import com.minecolonies.core.tileentities.TileEntityWareHouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import org.mcupdater.infohud.InfoHUDClient;
import org.mcupdater.infohud.api.HelperFunctions;
import org.mcupdater.infohud.minecolonies.events.ClientMonitor;
import org.mcupdater.infohud.minecolonies.records.CitizenDetail;
import org.mcupdater.infohud.minecolonies.records.CitizenRequest;
import org.mcupdater.infohud.minecolonies.records.ColonyIdentifier;
import org.mcupdater.infohud.minecolonies.InfoHUDMC;
import org.mcupdater.infohud.minecolonies.InfoHUDMCClient;
import org.mcupdater.infohud.setup.Config;
import org.mcupdater.infohud.tags.Tags;

import java.util.List;
import java.util.Map;

public class MCTags {
	public static Boolean isColonyPresent(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		return MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition());
	}

	public static String getColonyName(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			@Nullable IColony colony = MinecoloniesAPIProxy.getInstance().getColonyManager().getIColony(clientLevel, localPlayer.blockPosition());
			return colony.getName();
		} else {
			return "";
		}
	}

	public static Integer getColonyPopulation(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			return ClientMonitor.citizens.size();
		} else {
			return -1;
		}
	}

	public static Integer getColonyMaxPopulation(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			@Nullable IColony colony = MinecoloniesAPIProxy.getInstance().getColonyManager().getIColony(clientLevel, localPlayer.blockPosition());
			if (colony instanceof IColonyView colonyView) {
				return colonyView.getCitizenCountLimit();
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	public static Long getSickColonists(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			return List.copyOf(ClientMonitor.citizens).stream().filter(CitizenDetail::sick).count();
		} else {
			return -1L;
		}
	}

	public static Long getUnemployed(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			return List.copyOf(ClientMonitor.citizens).stream().filter(citizen -> citizen.job().isEmpty()).count();
		} else {
			return -1L;
		}
	}

	public static Long getChildren(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			return List.copyOf(ClientMonitor.citizens).stream().filter(CitizenDetail::child).count();
		} else {
			return -1L;
		}
	}

	public static Long getStarving(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			return List.copyOf(ClientMonitor.citizens).stream().filter(CitizenDetail::starving).count();
		} else {
			return -1L;
		}
	}

	public static Long getStudents(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			return List.copyOf(ClientMonitor.citizens).stream().filter(citizenDetail -> citizenDetail.job().equals("com.minecolonies.job.student")).count();
		} else {
			return -1L;
		}
	}

	public static Integer getWorkorders(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_resourcescroll", false))
		) {
			return -1;
		}
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			return ClientMonitor.workorders.size();
		} else {
			return -1;
		}
	}

	public static String getWorkorderFull(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 1);
		int index = Integer.valueOf(parts[1]);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_resourcescroll", false))
		) {
			return "";
		}
		if (index < ClientMonitor.workorders.size()) {
			var workorder = ClientMonitor.workorders.get(index);
			return workorder.claimedBy() + " [" + workorder.detail() + ": " + HelperFunctions.translateInternal(getStageName(workorder.stage()), localPlayer) + "]";
		} else {
			return "";
		}
	}

	public static Boolean getWorkorderExists(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 1);
		int index = Integer.valueOf(parts[1]);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_resourcescroll", false))
		) {
			return false;
		}
		return index < ClientMonitor.workorders.size();
	}

	public static String getWorkorderOwner(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 1);
		int index = Integer.valueOf(parts[1]);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_resourcescroll", false))
		) {
			return "";
		}
		if (index < ClientMonitor.workorders.size()) {
			var workorder = ClientMonitor.workorders.get(index);
			return workorder.claimedBy();
		} else {
			return "";
		}
	}

	public static String getWorkorderDetail(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 1);
		int index = Integer.valueOf(parts[1]);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_resourcescroll",false))
		) {
			return "";
		}
		if (index < ClientMonitor.workorders.size()) {
			var workorder = ClientMonitor.workorders.get(index);
			return workorder.detail();
		} else {
			return "";
		}
	}

	public static String getWorkorderLocation(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 1);
		int index = Integer.valueOf(parts[1]);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_resourcescroll", false))
		) {
			return "";
		}
		if (index < ClientMonitor.workorders.size()) {
			var workorder = ClientMonitor.workorders.get(index);
			return workorder.location().toShortString();
		} else {
			return "";
		}
	}

	public static String getWorkorderStage(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 1);
		int index = Integer.valueOf(parts[1]);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_resourcescroll",false))
		) {
			return "";
		}
		if (index < ClientMonitor.workorders.size()) {
			var workorder = ClientMonitor.workorders.get(index);
			return HelperFunctions.translateInternal(getStageName(workorder.stage()), localPlayer);
		} else {
			return "";
		}
	}

	public static String getWillRaid(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_tome", false))
		) {
			return "unknown";
		}
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			@Nullable IColony colony = MinecoloniesAPIProxy.getInstance().getColonyManager().getIColony(clientLevel, localPlayer.blockPosition());
			if (colony != null) {
				Boolean willRaid = InfoHUDMCClient.localIncomingRaidMap.getOrDefault(new ColonyIdentifier(colony.getDimension().location(), colony.getID()), null);
				if (willRaid == null) {
					return "unknown";
				} else {
					return willRaid.toString();
				}
			}
		}
		return "unknown";
	}

	private static String getStageName(BuildingProgressStage stage) {
		switch (stage) {
			case CLEAR:
				return "infohudmc.stage.clear";
			case SPAWN:
				return "infohudmc.stage.spawn";
			case REMOVE:
				return "infohudmc.stage.remove";
			case DECORATE:
				return "infohudmc.stage.decorate";
			case WEAK_SOLID:
				return "infohudmc.stage.weak_solid";
			case BUILD_SOLID:
				return "infohudmc.stage.build_solid";
			case CLEAR_WATER:
				return "infohudmc.stage.clear_water";
			case REMOVE_WATER:
				return "infohudmc.stage.remove_water";
			case CLEAR_NON_SOLIDS:
				return "infohudmc.stage.clear_non_solids";
			default:
				return "";
		}
	}

	public static Integer getWarehouseFreeSlots(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 1);
		int index = Integer.parseInt(parts[1]);
		if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(clientLevel, localPlayer.blockPosition())) {
			@Nullable IColony colony = MinecoloniesAPIProxy.getInstance().getColonyManager().getIColony(clientLevel, localPlayer.blockPosition());
			ColonyIdentifier identifier = new ColonyIdentifier(clientLevel.dimension().location(), colony.getID());
			if (!InfoHUDMCClient.localFreeWarehouseSlots.isEmpty()) {
				return InfoHUDMCClient.localFreeWarehouseSlots.get(identifier).getOrDefault(index, -1);
			}
			if (colony instanceof IColonyView colonyView) {
				List<IBuildingView> warehouses = colonyView.getClientBuildingManager().getBuildings().values().stream().filter(buildingView -> buildingView instanceof BuildingWareHouse.View).toList();
				if (warehouses.size() > index) {
					if (clientLevel.getBlockEntity(warehouses.get(index).getID()) instanceof TileEntityWareHouse warehouse) {
						@Nullable IItemHandler cap = warehouse.getItemHandlerCap();
						if (cap != null) {
							int freeSlots = 0;
							int slots = cap.getSlots();
							for (int slot = 0; slot < slots; slot++) {
								if (cap.getStackInSlot(slot).isEmpty()) {
									freeSlots++;
								}
							}
							return freeSlots;
						}
						return -1;
					}
				} else {
					return -1;
				}
			}
		}
		return -1;
	}

	public static Integer getRequests(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 0);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_clipboard", false))
		) {
			return -1;
		}
		return ClientMonitor.requests.size();
	}

	public static String getRequest(String[] parts, Minecraft minecraft, ClientLevel clientLevel, LocalPlayer localPlayer, float partialTick) {
		HelperFunctions.checkArgCount(parts, 1);
		int index = Integer.valueOf(parts[1]);
		if (
				((Config.REQUIRE_ITEMS.get() || InfoHUDClient.serverRequiresItems) && !InfoHUDClient.localStatus.status().getOrDefault("mc_clipboard",false))
		) {
			return "";
		}
		List<CitizenRequest> requests = List.copyOf(ClientMonitor.requests);
		if (requests.size() > index) {
			CitizenRequest request = requests.get(index);
			return request.name() + ": " + request.request();
		} else {
			return "";
		}
	}
}
