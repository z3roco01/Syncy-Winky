package z3roco01.syncywinky;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncyWinkyClient implements ClientModInitializer {
	public static final String MOD_ID = "syncy-winky";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("syncy winky !!");
	}
}