package org.mcupdater.infohud.minecolonies.records;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ColonyIdentifier(ResourceLocation dimension, Integer id) {
	public static final StreamCodec<ByteBuf, ColonyIdentifier> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC,
			ColonyIdentifier::dimension,
			ByteBufCodecs.INT,
			ColonyIdentifier::id,
			ColonyIdentifier::new
	);
}
