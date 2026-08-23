package org.mcupdater.infohud.minecolonies.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MCConfig {
	public static ModConfigSpec SERVER_CONFIG;
	public static final ModConfigSpec.IntValue TICK_RATE;

	static {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		TICK_RATE = builder.defineInRange("tick_rate", 20, 1, Integer.MAX_VALUE);
		SERVER_CONFIG = builder.build();
	}
}
