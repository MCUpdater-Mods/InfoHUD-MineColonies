package org.mcupdater.infohud.minecolonies.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.mcupdater.infohud.minecolonies.records.ColonyIdentifier;
import org.mcupdater.infohud.minecolonies.InfoHUDMC;

import java.util.HashMap;
import java.util.Map;

public record WarehouseMonitor(Map<ColonyIdentifier, Map<Integer,Integer>> freeWarehouseSlots) implements CustomPacketPayload {
	public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(InfoHUDMC.MODID, "warehouse_monitor");
	public static final CustomPacketPayload.Type<WarehouseMonitor> TYPE = new CustomPacketPayload.Type<>(LOCATION);

	public static final StreamCodec<ByteBuf, WarehouseMonitor> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.map(
					HashMap::new,
					ColonyIdentifier.STREAM_CODEC,
					ByteBufCodecs.map(
							HashMap::new,
							ByteBufCodecs.INT,
							ByteBufCodecs.INT
					)
			),
			WarehouseMonitor::freeWarehouseSlots,
			WarehouseMonitor::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
