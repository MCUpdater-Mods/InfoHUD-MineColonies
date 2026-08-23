package org.mcupdater.infohud.minecolonies.records;

import net.minecraft.core.BlockPos;

public record CitizenDetail(String name, BlockPos workplace, String job, Boolean sick, Boolean child, Boolean starving) {
}
