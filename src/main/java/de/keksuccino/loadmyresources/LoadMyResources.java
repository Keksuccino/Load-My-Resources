package de.keksuccino.loadmyresources;

import de.keksuccino.loadmyresources.pack.PackHandler;
import de.keksuccino.loadmyresources.utils.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod("loadmyresources")
public class LoadMyResources {

    public static final String VERSION = "1.0.0";
    
    public static final File HOME_DIR = new File("config/loadmyresources/");

    public static final Logger LOGGER = LogManager.getLogger();

    public static Config config;

    public static boolean init() {

        if (FMLEnvironment.dist == Dist.CLIENT) {

            if (!HOME_DIR.exists()) {
                HOME_DIR.mkdirs();
            }

            updateConfig();

            PackHandler.init();

//            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterReloadListeners);

//            MinecraftForge.EVENT_BUS.register(new Test());

            return true;

        } else {
            LOGGER.warn("## WARNING ## 'Load My Resources' is a client mod and has no effect when loaded on a server!");
        }

        return false;

    }

//    //Forge Only - Mixin needed in Fabric: Add 'LoadMyRepositorySource' instance to 'providers' field in TAIL of 'ResourcePackManager' constructor
//    private void onRegisterReloadListeners(RegisterClientReloadListenersEvent e) {
//
//        Minecraft.getInstance().getResourcePackRepository().addPackFinder(new LMRRepositorySource());
//        Minecraft.getInstance().getResourcePackRepository().reload();
//        System.out.println("[LOAD MY RESOURCES] Pack registered!");
//
//    }

    public static void updateConfig() {

        try {

            config = new Config(HOME_DIR.getPath() + "/config.cfg");

            //---------------------

            config.registerValue("resource_path", "resources/", "general", "The path to load resources from.");

            //---------------------

            config.syncConfig();

            config.clearUnusedValues();

        } catch (Exception e) {
            printStackTrace(e);
        }

    }

    public static void printStackTrace(Exception e) {
        for (StackTraceElement s : e.getStackTrace()) {
            LOGGER.error(s.toString());
        }
    }

}
