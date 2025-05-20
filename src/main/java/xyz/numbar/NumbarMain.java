package xyz.numbar;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumbarMain implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("numbar");


	@Override
	public void onInitialize() {
		LOGGER.info("Trans rights!");
	}
}