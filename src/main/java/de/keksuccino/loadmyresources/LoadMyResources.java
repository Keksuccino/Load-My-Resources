package de.keksuccino.loadmyresources;

import de.keksuccino.loadmyresources.pack.PackHandler;
import de.keksuccino.loadmyresources.utils.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class LoadMyResources implements ModInitializer {

    public static final String VERSION = "1.0.0";
    
    public static final File HOME_DIR = new File("config/loadmyresources/");

    public static Config config;

    public static boolean init() {

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {

            if (!HOME_DIR.exists()) {
                HOME_DIR.mkdirs();
            }

            updateConfig();

            PackHandler.init();

            return true;

        } else {
            System.out.println("## WARNING ## 'Load My Resources' is a client mod and has no effect when loaded on a server!");
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
