package xyz.numbar;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.numbar.config.NumbarConfig;

public class NumbarMain implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("numbar");


	@Override
	public void onInitializeClient() {
		AutoConfig.register(NumbarConfig.class, Toml4jConfigSerializer::new);
		LOGGER.info("Trans rights!");
	}
}