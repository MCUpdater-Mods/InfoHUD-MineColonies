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

public record RaidMonitor(Map<ColonyIdentifier,Boolean> incomingRaids) implements CustomPacketPayload {
	public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(InfoHUDMC.MODID, "raid_monitor");
	public static final CustomPacketPayload.Type<RaidMonitor> TYPE = new CustomPacketPayload.Type<>(LOCATION);

	public static final StreamCodec<ByteBuf, RaidMonitor> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.map(
					HashMap::new,
					ColonyIdentifier.STREAM_CODEC,
					ByteBufCodecs.BOOL
			),
			RaidMonitor::incomingRaids,
			RaidMonitor::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

}
