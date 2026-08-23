package org.mcupdater.infohud.minecolonies.records;

import net.minecraft.core.BlockPos;

public record CitizenRequest(String name, BlockPos hutLocation, String request) {
}
