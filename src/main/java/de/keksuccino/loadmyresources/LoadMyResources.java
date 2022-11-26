package de.keksuccino.loadmyresources;

import de.keksuccino.loadmyresources.pack.LMRRepositorySource;
import de.keksuccino.loadmyresources.pack.PackHandler;
import de.keksuccino.loadmyresources.utils.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod("loadmyresources")
public class LoadMyResources {

    //TODO übernehmen
    public static final String VERSION = "1.0.4";

    //TODO übernehmen
    public static final File HOME_DIR = new File(getGameDirectory(), "config/loadmyresources/");

    public static final Logger LOGGER = LogManager.getLogger();

    public static Config config;

    public LoadMyResources() {

        if (FMLEnvironment.dist == Dist.CLIENT) {

            if (!HOME_DIR.exists()) {
                HOME_DIR.mkdirs();
            }

            updateConfig();

            PackHandler.init();

            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterResourcePacks);

//            MinecraftForge.EVENT_BUS.register(new Test());

        } else {
            LOGGER.warn("## WARNING ## 'Load My Resources' is a client mod and has no effect when loaded on a server!");
        }

    }

    private void onRegisterResourcePacks(AddPackFindersEvent e) {
        e.addRepositorySource(new LMRRepositorySource());
        LOGGER.info("[LOAD MY RESOURCES] Pack registered!");
    }

    public static void updateConfig() {

        try {

            config = new Config(HOME_DIR.getAbsolutePath() + "/config.cfg");

            //---------------------

            config.registerValue("resource_path", "resources/", "general", "The path to load resources from.");

            //---------------------

            config.syncConfig();

            config.clearUnusedValues();

        } catch (Exception e) {
            printStackTrace(e);
        }

    }

    //In Forge 1.17, Errors don't get printed to the log for some reason, so this is my ugly workaround for now until I figure out why tf this happens
    public static void printStackTrace(Exception e) {
        for (StackTraceElement s : e.getStackTrace()) {
            LOGGER.error(s.toString());
        }
    }

    //TODO übernehmen
    public static File getGameDirectory() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return Minecraft.getInstance().gameDirectory;
        } else {
            return new File("");
        }
    }

}
