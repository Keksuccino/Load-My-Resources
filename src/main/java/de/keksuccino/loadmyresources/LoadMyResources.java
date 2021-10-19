package de.keksuccino.loadmyresources;

import de.keksuccino.loadmyresources.pack.PackHandler;
import de.keksuccino.loadmyresources.utils.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class LoadMyResources implements ModInitializer {

    public static final String VERSION = "1.0.1";
    
    public static final File HOME_DIR = new File("config/loadmyresources/");

    public static final Logger LOGGER = LogManager.getLogger();

    public static Config config;

    private static boolean isInitialized = false;

    public static boolean init() {

        if (isInitialized) {
            return true;
        }

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {

            if (!HOME_DIR.exists()) {
                HOME_DIR.mkdirs();
            }

            updateConfig();

            PackHandler.init();

            isInitialized = true;

            return true;

        } else {
            LOGGER.info("## WARNING ## 'Load My Resources' is a client mod and has no effect when loaded on a server!");
        }

        return false;

    }

    @Override
    public void onInitialize() {
    }

    public static void updateConfig() {

        try {

            config = new Config(HOME_DIR.getPath() + "/config.cfg");

            //---------------------

            config.registerValue("resource_path", "resources/", "general", "The path to load resources from.");

            //---------------------

            config.syncConfig();

            config.clearUnusedValues();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
