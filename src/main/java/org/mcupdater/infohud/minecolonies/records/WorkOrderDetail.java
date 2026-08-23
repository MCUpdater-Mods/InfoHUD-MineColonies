package org.mcupdater.infohud.minecolonies.records;

import com.minecolonies.core.entity.ai.workers.util.BuildingProgressStage;
import net.minecraft.core.BlockPos;

public record WorkOrderDetail(String detail, String claimedBy, BlockPos location, BuildingProgressStage stage) {
}
