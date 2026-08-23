package org.mcupdater.infohud.minecolonies.events;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyView;
import com.minecolonies.api.colony.interactionhandling.ChatPriority;
import com.minecolonies.core.colony.interactionhandling.RequestBasedInteraction;
import com.minecolonies.core.colony.workorders.view.WorkOrderBuildingView;
import com.minecolonies.core.colony.workorders.view.WorkOrderDecorationView;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;
import org.mcupdater.infohud.api.HelperFunctions;
import org.mcupdater.infohud.minecolonies.InfoHUDMC;
import org.mcupdater.infohud.minecolonies.records.CitizenDetail;
import org.mcupdater.infohud.minecolonies.records.CitizenRequest;
import org.mcupdater.infohud.minecolonies.records.WorkOrderDetail;
import org.mcupdater.infohud.tags.Tags;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = InfoHUDMC.MODID, value = Dist.CLIENT)
public class ClientMonitor {
	public static List<CitizenRequest> requests = new ArrayList<>();
	public static List<CitizenDetail> citizens = new ArrayList<>();
	public static List<WorkOrderDetail> workorders = new ArrayList<>();

	@SubscribeEvent
	public static void clientTick(PlayerTickEvent.Post event) {
		if (event.getEntity().level().isClientSide && event.getEntity() instanceof LocalPlayer) {
			citizens.clear();
			requests.clear();
			workorders.clear();
			if (MinecoloniesAPIProxy.getInstance().getColonyManager().isCoordinateInAnyColony(event.getEntity().level(), event.getEntity().blockPosition())) {
				@Nullable IColony colony = MinecoloniesAPIProxy.getInstance().getColonyManager().getIColony(event.getEntity().level(), event.getEntity().blockPosition());
				if (colony instanceof IColonyView colonyView) {
					// Gather citizen details
					{
						colonyView.getCitizens().values().forEach(citizen -> {
							citizens.add(new CitizenDetail(citizen.getName(), citizen.getWorkBuilding(), citizen.getJob(), citizen.isSick(), citizen.isChild(), citizen.getSaturation() <= 2.0D));
						});
					}
					// Gather blocking requests
					{
						colonyView.getCitizens().values().stream().forEach(citizen -> {
							citizen.getOrderedInteractions().stream().filter(interaction -> interaction instanceof RequestBasedInteraction && interaction.getPriority().equals(ChatPriority.BLOCKING)).forEach(responseHandler -> {
								requests.add(new CitizenRequest(citizen.getName(), citizen.getWorkBuilding(), HelperFunctions.translateInternal(responseHandler.getInquiry().getString(), (LocalPlayer) event.getEntity())));
							});
						});
					}

					// Gather claimed workorders
					{
						colonyView.getWorkOrders().stream().filter(iWorkOrderView -> (iWorkOrderView instanceof WorkOrderBuildingView || iWorkOrderView instanceof WorkOrderDecorationView) && iWorkOrderView.isClaimed()).forEach(workorder -> {
							String workerName = citizens.stream().filter(detail -> (detail.workplace() != null && detail.workplace().equals(workorder.getClaimedBy()))).map(citizen -> citizen.name()).findFirst().orElse("<Vacant>");
							workorders.add(new WorkOrderDetail(workorder.getDisplayName().getString().replace("\n", " "), workerName, workorder.getLocation(), workorder.getStage()));
						});
					}
				}
			}
		}
	}
}
